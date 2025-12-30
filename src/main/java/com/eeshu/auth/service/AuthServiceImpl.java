package com.eeshu.auth.service;

import com.eeshu.auth.dto.LoginRequest;
import com.eeshu.auth.dto.LoginResponse;
import com.eeshu.auth.dto.RefreshTokenRequest;
import com.eeshu.auth.dto.UserDto;
import com.eeshu.auth.exception.UserAlreadyExistsException;
import com.eeshu.auth.model.RefreshToken;
import com.eeshu.auth.model.User;
import com.eeshu.auth.repository.RefreshTokenRepository;
import com.eeshu.auth.repository.UserRepository;
import com.eeshu.auth.security.CookieService;
import com.eeshu.auth.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;

    @Override
    public LoginResponse register(LoginRequest loginRequest) {
        //logic
        //verify email
        log.info("Registering user with email: {}", loginRequest.getEmail());
        if (userRepository.existsByEmail(loginRequest.getEmail())) {
            throw new UserAlreadyExistsException("User Already exist with email " + loginRequest.getEmail());
        }
        loginRequest.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
        User user = modelMapper.map(loginRequest, User.class);

//        Set<Role> role = new HashSet<>();
//        role.add(new Role("USER"));
//        user.setRoles(role);
//        System.out.println(user.toString()+"user");
//        System.out.println(user.getRoles().toString()+"user");

        User savUser = userRepository.save(user);
        String token = jwtService.generateToken(savUser);
        String email = savUser.getEmail();

        return LoginResponse.builder()

                .token(token)
                .userDto(modelMapper.map(savUser, UserDto.class))
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new BadCredentialsException("Bad credentials"));
            if (!user.isEnabled()) {
                throw new BadCredentialsException("User is not enabled");
            }
            //JTI ka matlab hota hai JWT ID. Ye har token ka ek unique aadhar card number jaisa hota hai.
//            JTI ka asli kaam kya hai?
//            Unique Identity: Ek hi user ke paas 2-3 tokens ho sakte hain (laptop aur phone ke liye). JTI se server ko pata chalta hai ki kaun sa token kis device ka hai.
//
//                    Chori se bachao (Security): Agar koi hacker aapka purana token use karne ki koshish kare, toh server check karta hai ki "kya ye jti pehle use ho chuka hai?" Agar haan, toh wo usse block kar deta hai.
//
//            Logout Feature: Jab aap "Logout from this device" par click karte ho, toh server sirf us specific jti wale token ko database se delete ya revoke (cancel) karta hai.
            String jti = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now();
            var refreshTokenEntity = RefreshToken.builder()
                    .jti(jti)
                    .user(user)
                    .createdAt(now)
                    .expiredAt(now.plusMinutes(30))
                    .revoked(false)
                    .build();
            RefreshToken savedToken = refreshTokenRepository.save(refreshTokenEntity);


            String accessToken = jwtService.generateToken(user);
            String refreshTokenString = jwtService.generateRefreshToken(user, refreshTokenEntity.getJti());
            //use cookie service to attach refresh token in cookie
            cookieService.attachRefreshCookie(response, refreshTokenString, 30 * 24 * 60 * 60);
            cookieService.addNoStoreHeaders(response);
            return LoginResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshTokenString)
                    .expiresIn(refreshTokenEntity.getExpiredAt())
                    .userDto(modelMapper.map(user, UserDto.class))
                    .build();


        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials");
        }

    }

    @Override
    public LoginResponse resfreshtoken(RefreshTokenRequest refreshTokenRequest, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String refreshToken = readRefreshTokenFromRequest(httpServletRequest, refreshTokenRequest).orElseThrow(() -> new BadCredentialsException("Invalid Refresh Token"));
        if(!jwtService.isRefreshToken(refreshToken)) {
            throw  new BadCredentialsException("Invalid Refresh Token type")
                    ;
        }
        String jti = jwtService.getJti(refreshToken);
        UUID userIdFromToken = jwtService.getUserIdFromToken(refreshToken);
        RefreshToken refreshTokenFromDb = refreshTokenRepository.findByJti(jti).orElseThrow(() -> new BadCredentialsException("Jti Not present in db"));
        if (refreshTokenFromDb.isRevoked()) {
            throw new BadCredentialsException("Refresh token is revoked");
        }
        if(refreshTokenFromDb.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw  new BadCredentialsException("Refresh token is expired");
        }
        if(!refreshTokenFromDb.getUser().getId().equals(userIdFromToken)) {
            throw  new BadCredentialsException("Refresh Token Does not belong to this user");
        }
        //refresh token rotation
        refreshTokenFromDb.setRevoked(true);
        String newJti = UUID.randomUUID().toString();
        refreshTokenFromDb.setReplacedByToken(newJti);
        User user = refreshTokenFromDb.getUser();
        var newRefreshTokenEntity = RefreshToken.builder()
                .jti(newJti)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .revoked(false)
                .build();
        refreshTokenRepository.save(newRefreshTokenEntity);

            String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user, newJti);
        cookieService.attachRefreshCookie(response, newRefreshToken, 30 * 24 * 60 * 60);
        cookieService.addNoStoreHeaders(response);
        LoginResponse loginResponse = LoginResponse.builder()
                .token(accessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(newRefreshTokenEntity.getExpiredAt())
                .userDto(modelMapper.map(user, UserDto.class))
                .build();
        log.info("Refresh token response: {}", loginResponse);
        return loginResponse;
    }


    private Optional<String> readRefreshTokenFromRequest(HttpServletRequest httpServletRequest, RefreshTokenRequest refreshTokenRequest) {

        if (httpServletRequest.getCookies() != null) {
            Optional<String> fromCookie = Arrays.stream(
                            httpServletRequest.getCookies()
                    ).filter(c -> cookieService.getRefreshTokenCookieName().equals(c.getName()))
                    .map(Cookie::getValue)
                    .filter(v -> !v.isBlank())
                    .findFirst();

            if (fromCookie.isPresent()) {
                return fromCookie;
            }
        }
        if (refreshTokenRequest != null && refreshTokenRequest.getRefreshToken() != null && !refreshTokenRequest.getRefreshToken().isBlank()) {
            return Optional.of(refreshTokenRequest.getRefreshToken());
        }
       // custom header
        String fromHeader = httpServletRequest.getHeader("X-Refresh-Token");
        if (fromHeader != null && !fromHeader.isBlank()) {
            return Optional.of(fromHeader);
        }

        return Optional.empty();
    }


}

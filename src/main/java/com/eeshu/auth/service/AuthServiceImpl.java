package com.eeshu.auth.service;

import com.eeshu.auth.dto.LoginRequest;
import com.eeshu.auth.dto.LoginResponse;
import com.eeshu.auth.dto.UserDto;
import com.eeshu.auth.exception.UserAlreadyExistsException;
import com.eeshu.auth.model.Role;
import com.eeshu.auth.model.User;
import com.eeshu.auth.repository.UserRepository;
import com.eeshu.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public LoginResponse register(LoginRequest loginRequest) {
        //logic
        //verify email
        log.info("Registering user with email: {}", loginRequest.getEmail());
        if (userRepository.existsByEmail(loginRequest.getEmail())) {
            throw new UserAlreadyExistsException("User Already exist with email "+loginRequest.getEmail());
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
    public LoginResponse login(LoginRequest loginRequest) {

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new BadCredentialsException("Bad credentials"));
            if (!user.isEnabled()) {
                throw new BadCredentialsException("User is not enabled");
            }
            String token = jwtService.generateToken(user);

            return LoginResponse.builder()
                    .token(token)
                    .refreshToken("no")
                    .expiresIn("expires in")
                    .userDto(UserDto
                            .builder()
                            .email(user.getEmail())
                            .build())
                    .build();


        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials");
        }

    }


}

package com.eeshu.auth.controller;

import com.eeshu.auth.dto.LoginRequest;
import com.eeshu.auth.dto.LoginResponse;
import com.eeshu.auth.dto.RefreshTokenRequest;
import com.eeshu.auth.repository.RefreshTokenRepository;
import com.eeshu.auth.response.ApiResponse;
import com.eeshu.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")

@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse register = authService.register(loginRequest);
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>("Success", "User registered successfully", register);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/signin")
    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> signin(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse res = authService.login(loginRequest, response);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Success", "SignIn Successfully", res));


    }

    @PostMapping("/refreshtoken")
    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> resfreshtoken(@RequestBody(required = false) RefreshTokenRequest refreshTokenRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LoginResponse resfreshtoken = authService.resfreshtoken(refreshTokenRequest, httpServletRequest, httpServletResponse);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Success", "Refresh Token Successfully", resfreshtoken));
    }
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
//
//        // 1. Request ki Cookies mein se Refresh Token nikalna
//        String refreshToken = cookieService.getCookieValue(request, cookieService.getRefreshTokenCookieName());
//
//        if (refreshToken != null) {
//            try {
//                // 2. Check karna ki ye naya wala "Refresh" token hi hai
//                if (jwtService.isRefreshToken(refreshToken)) {
//
//                    // 3. Token ke andar se uski unique ID (JTI) nikalna
//                    String jti = jwtService.getJti(refreshToken);
//
//                    // 4. DATABASE ACTION: Us specific JTI wale token ko 'revoked' mark karna
//                    refreshTokenRepository.findByJti(jti).ifPresent(tokenEntity -> {
//                        tokenEntity.setRevoked(true);
//                        refreshTokenRepository.save(tokenEntity);
//                        log.info("Successfully revoked token in DB for JTI: {}", jti);
//                    });
//                }
//            } catch (Exception e) {
//                // Agar token corrupt hai ya expire ho gaya hai, toh sirf log karein
//                log.warn("Logout: Could not process token revocation in DB: {}", e.getMessage());
//            }
//        }
//
//        // 5. BROWSER ACTION: Browser ko command dena ki 'refresh-token' cookie delete kar de
//        cookieService.clearRefreshCookie(response);
//
//        // 6. SECURITY: Browser cache clear karne ke headers (taaki back button dabane par data na dikhe)
//        cookieService.addNoStoreHeaders(response);
//
//        return ResponseEntity.noContent().build(); // 204 No Content return karein
//    }
}

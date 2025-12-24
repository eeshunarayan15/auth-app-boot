package com.eeshu.auth.controller;

import com.eeshu.auth.dto.LoginRequest;
import com.eeshu.auth.dto.LoginResponse;
import com.eeshu.auth.response.ApiResponse;
import com.eeshu.auth.service.AuthService;
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
    public ResponseEntity<ApiResponse<LoginResponse>> signin(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse res = authService.login(loginRequest);

                return ResponseEntity.status(HttpStatus.OK).body(   new ApiResponse<>("Success","SignIn Successfully",res));


    }
}

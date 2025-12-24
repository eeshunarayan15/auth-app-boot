package com.eeshu.auth.controller;

import org.springframework.http.ResponseEntity;

import com.eeshu.auth.dto.LoginRequest;
import com.eeshu.auth.dto.LoginResponse;
import com.eeshu.auth.response.ApiResponse;

public interface AuthController {
    public ResponseEntity<ApiResponse<LoginResponse>> register(LoginRequest loginRequest) ;
   ResponseEntity<ApiResponse<LoginResponse>> signin(LoginRequest loginRequest);
}

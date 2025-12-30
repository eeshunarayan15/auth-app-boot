package com.eeshu.auth.controller;

import com.eeshu.auth.dto.RefreshTokenRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import com.eeshu.auth.dto.LoginRequest;
import com.eeshu.auth.dto.LoginResponse;
import com.eeshu.auth.response.ApiResponse;

public interface AuthController {
    public ResponseEntity<ApiResponse<LoginResponse>> register(LoginRequest loginRequest) ;
   ResponseEntity<ApiResponse<LoginResponse>> signin(LoginRequest loginRequest , HttpServletResponse response);
   //access and refresh token renew karne ke liye api
    ResponseEntity<ApiResponse<LoginResponse>> resfreshtoken(RefreshTokenRequest refreshTokenRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}

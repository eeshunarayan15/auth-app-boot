package com.eeshu.auth.service;

import com.eeshu.auth.dto.LoginRequest;
import com.eeshu.auth.dto.LoginResponse;
import com.eeshu.auth.dto.RefreshTokenRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    //register user
   LoginResponse register(LoginRequest loginRequest);
   //login register
    LoginResponse login(LoginRequest loginRequest , HttpServletResponse response);


    LoginResponse resfreshtoken(RefreshTokenRequest refreshTokenRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}

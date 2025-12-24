package com.eeshu.auth.service;

import com.eeshu.auth.dto.LoginRequest;
import com.eeshu.auth.dto.LoginResponse;

public interface AuthService {
    //register user
   LoginResponse register(LoginRequest loginRequest);
   //login register
    LoginResponse login(LoginRequest loginRequest);
}

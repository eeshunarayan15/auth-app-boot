package com.eeshu.auth.service;

import com.eeshu.auth.dto.LoginRequest;
import com.eeshu.auth.dto.LoginResponse;
import com.eeshu.auth.model.User;
import com.eeshu.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements  AuthService {
     private final PasswordEncoder passwordEncoder;
     private final UserRepository userRepository;
     private final ModelMapper modelMapper;
    @Override
    public LoginResponse register(LoginRequest loginRequest) {
        //logic
        //verify email
        log.info("Registering user with email: {}", loginRequest.getEmail());
        if(userRepository.existsByEmail(loginRequest.getEmail())){
            throw new RuntimeException("Email already exists");
        }
  loginRequest.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
        User user = modelMapper.map(loginRequest, User.class);
     User savUser=  userRepository.save(user);
     String email = savUser.getEmail();
       return LoginResponse.builder()
       .email(email)
       .token("token")
       .build();
    }
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

}

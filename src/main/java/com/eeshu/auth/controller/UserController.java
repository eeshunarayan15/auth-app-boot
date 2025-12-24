package com.eeshu.auth.controller;

import com.eeshu.auth.dto.UserCreateRequest;
import com.eeshu.auth.dto.UserDto;
import com.eeshu.auth.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface UserController {
   ResponseEntity<ApiResponse<UserDto>> createUser(UserCreateRequest userCreateRequest);
   UserDto updateUser(UserDto userDto);
   UserDto deleteUser(UserDto userDto);
   UserDto getUserByEmail(String email);
   ResponseEntity<List<UserDto>> getAllUsers();

}

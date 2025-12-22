package com.eeshu.auth.controller;

import com.eeshu.auth.dto.UserCreateRequest;
import com.eeshu.auth.dto.UserDto;
import com.eeshu.auth.response.ApiResponse;
import com.eeshu.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserControllerImpl  implements UserController{
    private  final ModelMapper modelMapper;
    private  final UserService userService;
    @Override
    public ResponseEntity<ApiResponse<UserDto>> createUser(UserCreateRequest userCreateRequest) {
        UserDto user = userService.createUser(userCreateRequest);
        ApiResponse<UserDto> userResponse = new ApiResponse<>("Success", "User saved Successfully", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto deleteUser(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDto getAllUsers() {
        return null;
    }
}

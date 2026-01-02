package com.eeshu.auth.controller;

import com.eeshu.auth.dto.UserCreateRequest;
import com.eeshu.auth.dto.UserDto;
import com.eeshu.auth.response.ApiResponse;
import com.eeshu.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserControllerImpl  implements UserController{
  
    private  final UserService userService;
    @PostMapping("/users")
    @Override
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        System.out.println(userCreateRequest);
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

    @GetMapping("/users")
    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> allUsers = userService.getAllUsers();
        ApiResponse<List<UserDto>> listApiResponse = new ApiResponse<>("Sucess", "Users Found", allUsers);
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }
}

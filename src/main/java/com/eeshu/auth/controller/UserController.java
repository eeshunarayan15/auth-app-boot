package com.eeshu.auth.controller;

import com.eeshu.auth.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public interface UserController {
   UserDto createUser(UserDto userDto);
   UserDto updateUser(UserDto userDto);
   UserDto deleteUser(UserDto userDto);
   UserDto getUserByEmail(String email);

}

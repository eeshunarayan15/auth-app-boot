package com.eeshu.auth.service;

import com.eeshu.auth.dto.UserCreateRequest;
import com.eeshu.auth.dto.UserDto;


import java.util.List;

public interface UserService {
     UserDto createUser(UserCreateRequest userCreateRequest);
     List<UserDto> getAllUsers();
     UserDto updateUser(UserDto userDto);
}

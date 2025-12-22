package com.eeshu.auth.service;

import com.eeshu.auth.dto.UserCreateRequest;
import com.eeshu.auth.dto.UserDto;
import com.eeshu.auth.model.User;

public interface UserService {
     UserDto createUser(UserCreateRequest userCreateRequest);
     UserDto getAllUsers();
     UserDto updateUser(UserDto userDto);
}

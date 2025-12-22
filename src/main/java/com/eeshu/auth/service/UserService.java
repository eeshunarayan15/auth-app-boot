package com.eeshu.auth.service;

import com.eeshu.auth.dto.UserDto;
import com.eeshu.auth.model.User;

public interface UserService {
     User createUser(UserDto UserDto);
}

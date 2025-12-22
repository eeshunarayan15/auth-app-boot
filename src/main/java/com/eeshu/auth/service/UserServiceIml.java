package com.eeshu.auth.service;

import com.eeshu.auth.dto.UserCreateRequest;
import com.eeshu.auth.dto.UserDto;
import com.eeshu.auth.model.Provider;
import com.eeshu.auth.model.User;
import com.eeshu.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    @Override
    public UserDto createUser(UserCreateRequest userCreateRequest) {
        User user = modelMapper.map(userCreateRequest, User.class);
        if(user.getProvider()==null){
            user.setProvider(Provider.LOCAL);
        }
        User savedUser = userRepository.save(user);


        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getAllUsers() {
        List<User> all = userRepository.findAll();
return (UserDto) all.stream().map(user->modelMapper.map(user,UserDto.class))
        .toList();

    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return null;
    }
}

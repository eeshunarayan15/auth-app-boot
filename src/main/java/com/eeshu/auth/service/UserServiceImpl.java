package com.eeshu.auth.service;

import com.eeshu.auth.dto.AddressDto;
import com.eeshu.auth.dto.UserCreateRequest;
import com.eeshu.auth.dto.UserDto;
import com.eeshu.auth.model.Provider;
import com.eeshu.auth.model.Role;
import com.eeshu.auth.model.User;
import com.eeshu.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
@Transactional
    @Override
    public UserDto createUser(UserCreateRequest userCreateRequest) {
        User user = modelMapper.map(userCreateRequest, User.class);
        if (user.getProvider() == null) {
            user.setProvider(Provider.LOCAL);
        }
        User savedUser = userRepository.save(user);


        return UserDto.builder()
                .id(savedUser.getId().toString())
                .email(savedUser.getEmail())
                .address(savedUser.getAddress() != null ?
                        AddressDto.builder()
                                .addressLine1(savedUser.getAddress().getAddressLine1())
                                .addressLine2(savedUser.getAddress().getAddressLine2())
                                .city(savedUser.getAddress().getCity())
                                .state(savedUser.getAddress().getState())
                                .country(savedUser.getAddress().getCountry())
                                .postalCode(savedUser.getAddress().getPostalCode())
                                .build()
                        : null)
                .createdAt(savedUser.getCreatedAt())
                .enabled(savedUser.isEnabled())
                .phone(savedUser.getPhone())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .username(savedUser.getUsername())
                .roles(savedUser.getRoles() != null ?
                        savedUser.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet()) : null)
                .build();
    }

    @Override
    public List<UserDto> getAllUsers() {
     return  userRepository.findAll().stream().map(user->
             UserDto.builder()
                     .id(user.getId().toString())
                     .email(user.getEmail())
                     .address(user.getAddress() != null ?
                             AddressDto.builder()
                                     .addressLine1(user.getAddress().getAddressLine1())
                                     .addressLine2(user.getAddress().getAddressLine2())
                                     .city(user.getAddress().getCity())
                                     .state(user.getAddress().getState())
                                     .country(user.getAddress().getCountry())
                                     .postalCode(user.getAddress().getPostalCode())
                                     .build()
                             : null)
                     .createdAt(user.getCreatedAt())
                     .enabled(user.isEnabled())
                     .phone(user.getPhone())
                     .firstName(user.getFirstName())
                     .lastName(user.getLastName())
                     .username(user.getUsername())
                     .roles(user.getRoles() != null ?
                             user.getRoles().stream()
                                     .map(Role::getName)
                                     .collect(Collectors.toSet()) : null)
                     .build()

             ).collect(Collectors.toList());


    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return null;
    }
}

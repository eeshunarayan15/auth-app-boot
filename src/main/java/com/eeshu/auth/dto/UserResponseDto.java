package com.eeshu.auth.dto;

import com.eeshu.auth.model.Provider;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String id;
    private String username;
    private String email;

    private String firstName;
    private String lastName;
    private String phone;

    private AddressDto address;

    private Provider provider;
    private boolean enabled;

    private Set<String> roles;

    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}

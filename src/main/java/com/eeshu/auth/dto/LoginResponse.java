package com.eeshu.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    String token;
    String refreshToken;
    String expiresIn;
    String tokenType;

    UserDto userDto;


}

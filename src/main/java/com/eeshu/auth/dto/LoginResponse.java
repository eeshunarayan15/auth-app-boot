package com.eeshu.auth.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    
    LocalDateTime expiresIn;
    String tokenType;
    UserDto userDto;
}

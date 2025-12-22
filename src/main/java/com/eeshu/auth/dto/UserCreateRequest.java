package com.eeshu.auth.dto;

import com.eeshu.auth.model.Provider;

public class UserCreateRequest {

    private String username;
    private String email;
    private String password;   // only for LOCAL
    private String firstName;
    private String lastName;
    private String phone;
    private AddressDto address;
    private Provider provider; // OPTIONAL (OAuth only)
}

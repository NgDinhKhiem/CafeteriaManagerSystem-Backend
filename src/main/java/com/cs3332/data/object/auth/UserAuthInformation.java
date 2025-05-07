package com.cs3332.data.object.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserAuthInformation {
    private final String username;
    private final String password;
}


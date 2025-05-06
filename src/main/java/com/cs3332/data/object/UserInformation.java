package com.cs3332.data.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserInformation {
    private final String username;
    private final String name;
    private final String email;
    private final String phone;
    private final long dateOfBirth;
    private final String gender;
    private final List<String> roles;
}


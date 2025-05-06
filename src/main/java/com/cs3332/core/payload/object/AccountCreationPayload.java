package com.cs3332.core.payload.object;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AccountCreationPayload extends AbstractRequestBody {
    private final String username;
    private final String password;
    private final String name;
    private final String email;
    private final String phone;
    private final long dateOfBirth;
    private final String gender;
    private final List<String> roles;
}

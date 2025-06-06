package com.cs3332.core.payload.object.auth;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class LoginPayload extends AbstractRequestBody {
    private final String username;
    @Setter
    private String password;
}

package com.cs3332.core.response.object;

import com.cs3332.core.response.constructor.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse extends AbstractResponse {
    private final String error;
}

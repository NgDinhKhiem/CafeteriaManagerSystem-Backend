package com.cs3332.core.response.object.cart;

import com.cs3332.core.response.AbstractResponse;
import lombok.Getter;

@Getter
public class CartResponse extends AbstractResponse {
    private final String message;
    private final boolean success;

    public CartResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
} 
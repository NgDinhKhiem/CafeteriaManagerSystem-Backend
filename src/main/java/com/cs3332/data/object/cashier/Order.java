package com.cs3332.data.object.cashier;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Order {
    private final UUID productId;
    private final long quantity;
    private final String description;
}


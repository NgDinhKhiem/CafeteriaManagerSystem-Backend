package com.cs3332.data.object.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Sale {
    private final UUID ID;
    private final UUID productID;
    private final String price;
    private final int amount;
    private final long timeStamp;
}
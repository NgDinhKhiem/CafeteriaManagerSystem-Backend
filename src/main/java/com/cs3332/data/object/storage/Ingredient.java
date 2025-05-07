package com.cs3332.data.object.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Ingredient {
    private final UUID itemStackID;
    private final float quantity;
}


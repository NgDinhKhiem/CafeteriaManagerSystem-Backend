package com.cs3332.data.object.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ItemStack {
    private final UUID ID;
    private final String name;
    private final String unit;
}


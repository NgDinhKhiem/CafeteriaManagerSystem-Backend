package com.cs3332.data.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProductInformation {
    private final String ID;
    private final String name;
    private final String unit;
    private final float price;
    private final float amount;
}


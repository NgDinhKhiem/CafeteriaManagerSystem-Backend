package com.cs3332.data.object.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private UUID ID;
    private String name;
    private String unit;
    private List<Ingredient> recipe;
    private float price;
}


package com.cs3332.data.object.storage;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void testProductConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Coffee";
        String unit = "cup";
        Ingredient ingredient = new Ingredient(UUID.randomUUID(), 2.0f);
        List<Ingredient> recipe = List.of(ingredient);
        float price = 3.5f;

        Product product = new Product(id, name, unit, recipe, price);

        assertEquals(id, product.getID());
        assertEquals(name, product.getName());
        assertEquals(unit, product.getUnit());
        assertEquals(recipe, product.getRecipe());
        assertEquals(price, product.getPrice());
    }
} 
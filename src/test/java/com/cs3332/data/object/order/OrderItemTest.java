package com.cs3332.data.object.order;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {
    @Test
    void testOrderItemConstructorAndGetters() {
        UUID productID = UUID.randomUUID();
        String productName = "Test Product";
        String unit = "piece";
        int quantity = 3;
        double priceAtOrder = 9.99;

        OrderItem item = new OrderItem(productID, productName, unit, quantity, priceAtOrder);

        assertEquals(productID, item.getProductID());
        assertEquals(productName, item.getProductName());
        assertEquals(unit, item.getUnit());
        assertEquals(quantity, item.getQuantity());
        assertEquals(priceAtOrder, item.getPriceAtOrder());
    }
} 
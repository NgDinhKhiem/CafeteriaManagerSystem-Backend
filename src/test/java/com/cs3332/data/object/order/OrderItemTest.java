package com.cs3332.data.object.order;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {
    @Test
    void testOrderItemConstructorAndGetters() {
        UUID productID = UUID.randomUUID();
        int quantity = 3;
        double priceAtOrder = 9.99;

        OrderItem item = new OrderItem(productID, quantity, priceAtOrder);

        assertEquals(productID, item.getProductID());
        assertEquals(quantity, item.getQuantity());
        assertEquals(priceAtOrder, item.getPriceAtOrder());
    }
} 
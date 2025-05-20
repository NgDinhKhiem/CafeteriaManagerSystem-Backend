package com.cs3332.data.object.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private UUID productID;
    private int quantity;
    private double priceAtOrder;
} 
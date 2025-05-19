package com.cs3332.data.object.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private UUID orderID;
    private List<OrderItem> items;
    private double totalAmount;
    private long orderTimestamp;
    private OrderStatus status;
    private String userID; // Username of the cashier/bartender
    @Nullable
    private Long paymentTimestamp;
} 
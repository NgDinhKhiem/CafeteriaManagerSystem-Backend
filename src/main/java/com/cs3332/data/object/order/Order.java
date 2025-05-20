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
    @Nullable
    private Long preparationStartTimestamp; // When bartender starts preparing
    @Nullable
    private Long readyTimestamp; // When order is ready for pickup
    @Nullable
    private Long completionTimestamp; // When order is picked up/delivered
    @Nullable
    private String notes; // Special instructions or notes for the order
    @Nullable
    private String preparedBy; // Username of the bartender who prepared the order
} 
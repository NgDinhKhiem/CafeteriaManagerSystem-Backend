package com.cs3332.data.object.order;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING_CONFIRMATION(0),
    PENDING_PAYMENT(1),    // Initial state when order is created
    PAID(2),              // Payment received
    PREPARING(2),         // Order is being prepared by bartender
    READY(3),             // Order is ready for pickup
    COMPLETED(4),         // Order has been picked up/delivered
    CANCELLED(4);
    // Order was cancelled
    private final int weight;
}
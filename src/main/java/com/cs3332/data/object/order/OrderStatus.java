package com.cs3332.data.object.order;

public enum OrderStatus {
    PENDING_PAYMENT,    // Initial state when order is created
    PAID,              // Payment received
    PREPARING,         // Order is being prepared by bartender
    READY,             // Order is ready for pickup
    COMPLETED,         // Order has been picked up/delivered
    CANCELLED          // Order was cancelled
} 
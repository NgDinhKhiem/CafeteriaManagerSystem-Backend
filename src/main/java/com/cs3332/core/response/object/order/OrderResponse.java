package com.cs3332.core.response.object.order;

import com.cs3332.core.response.constructor.AbstractResponse;
import com.cs3332.data.object.order.OrderItem;
import com.cs3332.data.object.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse extends AbstractResponse {
    private UUID orderID;
    private List<OrderItem> items;
    private double totalAmount;
    private long orderTimestamp;
    private OrderStatus status;
    private String userID; // Username of the cashier/bartender who created/handled the order
    @Nullable
    private Long paymentTimestamp;
    @Nullable
    private Long preparationStartTimestamp;
    @Nullable
    private Long readyTimestamp;
    @Nullable
    private Long completionTimestamp;
    @Nullable
    private String notes;
    @Nullable
    private String preparedBy;
}
package com.cs3332.core.response.object.order;

import com.cs3332.core.response.constructor.AbstractResponse;
import com.cs3332.data.object.order.OrderItem;
import com.cs3332.data.object.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse extends AbstractResponse {
    private String tableID;
    private UUID orderID;
    private List<OrderItem> items;
    private long orderTimestamp;
    private OrderStatus status;
    private String userID;
    private Long paymentTimestamp;
    private Long readyTimestamp;
    private Long completionTimestamp;
    private String preparedBy;
}
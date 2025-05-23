package com.cs3332.data.object.order;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
//    @Test
//    void testOrderConstructorAndGetters() {
//        String tableID = "T1";
//        UUID orderID = UUID.randomUUID();
//        OrderItem item = new OrderItem(UUID.randomUUID(), 2, 5.0);
//        long orderTimestamp = System.currentTimeMillis();
//        OrderStatus status = OrderStatus.PAID;
//        String userID = "cashier1";
//        Long paymentTimestamp = orderTimestamp + 1000;
//        Long preparationStartTimestamp = orderTimestamp + 2000;
//        Long readyTimestamp = orderTimestamp + 3000;
//        String preparedBy = "bartender1";
//
//        Order order = new Order(tableID, orderID, List.of(item), orderTimestamp, status, userID, paymentTimestamp, preparationStartTimestamp, readyTimestamp, preparedBy);
//
//        assertEquals(tableID, order.getTableID());
//        assertEquals(orderID, order.getOrderID());
//        assertEquals(1, order.getItems().size());
//        assertEquals(orderTimestamp, order.getOrderTimestamp());
//        assertEquals(status, order.getStatus());
//        assertEquals(userID, order.getUserID());
//        assertEquals(paymentTimestamp, order.getPaymentTimestamp());
//        assertEquals(preparationStartTimestamp, order.getPreparationStartTimestamp());
//        assertEquals(readyTimestamp, order.getReadyTimestamp());
//        assertEquals(preparedBy, order.getPreparedBy());
//    }
//
//    @Test
//    void testSumOfItemsInOrder() {
//        OrderItem item1 = new OrderItem(UUID.randomUUID(), 2, 5.0);
//        OrderItem item2 = new OrderItem(UUID.randomUUID(), 3, 7.5);
//        Order order = new Order(
//                "T2",
//                UUID.randomUUID(),
//                List.of(item1, item2),
//                System.currentTimeMillis(),
//                OrderStatus.PAID,
//                "cashier2",
//                0L, 0L, 0L, ""
//        );
//        int sum = order.getItems().stream().mapToInt(OrderItem::getQuantity).sum();
//        assertEquals(5, sum);
//    }
//
//    @Test
//    void testSumOfItemsInAllOrders() {
//        OrderItem item1 = new OrderItem(UUID.randomUUID(), 2, 5.0);
//        OrderItem item2 = new OrderItem(UUID.randomUUID(), 3, 7.5);
//        OrderItem item3 = new OrderItem(UUID.randomUUID(), 4, 8.0);
//        Order order1 = new Order("T1", UUID.randomUUID(), List.of(item1, item2), System.currentTimeMillis(), OrderStatus.PAID, "cashier1", 0L, 0L, 0L, "");
//        Order order2 = new Order("T2", UUID.randomUUID(), List.of(item3), System.currentTimeMillis(), OrderStatus.PAID, "cashier2", 0L, 0L, 0L, "");
//        List<Order> orders = List.of(order1, order2);
//        int totalSum = orders.stream().flatMap(o -> o.getItems().stream()).mapToInt(OrderItem::getQuantity).sum();
//        assertEquals(9, totalSum);
//    }
//
//    @Test
//    void testSumTotalPriceInOrder() {
//        OrderItem item1 = new OrderItem(UUID.randomUUID(), 2, 5.0);
//        OrderItem item2 = new OrderItem(UUID.randomUUID(), 3, 7.5);
//        Order order = new Order("T3", UUID.randomUUID(), List.of(item1, item2), System.currentTimeMillis(), OrderStatus.PAID, "cashier3", 0L, 0L, 0L, "");
//        double total = order.getItems().stream().mapToDouble(i -> i.getQuantity() * i.getPriceAtOrder()).sum();
//        assertEquals(2*5.0 + 3*7.5, total, 0.0001);
//    }
//
//    @Test
//    void testSumTotalPriceAcrossAllOrders() {
//        OrderItem item1 = new OrderItem(UUID.randomUUID(), 2, 5.0);
//        OrderItem item2 = new OrderItem(UUID.randomUUID(), 3, 7.5);
//        OrderItem item3 = new OrderItem(UUID.randomUUID(), 4, 8.0);
//        Order order1 = new Order("T1", UUID.randomUUID(), List.of(item1, item2), System.currentTimeMillis(), OrderStatus.PAID, "cashier1", 0L, 0L, 0L, "");
//        Order order2 = new Order("T2", UUID.randomUUID(), List.of(item3), System.currentTimeMillis(), OrderStatus.PAID, "cashier2", 0L, 0L, 0L, "");
//        List<Order> orders = List.of(order1, order2);
//        double total = orders.stream().flatMap(o -> o.getItems().stream()).mapToDouble(i -> i.getQuantity() * i.getPriceAtOrder()).sum();
//        assertEquals(2*5.0 + 3*7.5 + 4*8.0, total, 0.0001);
//    }
//
//    @Test
//    void testSumWithEmptyOrder() {
//        Order order = new Order("T4", UUID.randomUUID(), List.of(), System.currentTimeMillis(), OrderStatus.PAID, "cashier4", 0L, 0L, 0L, "");
//        int sum = order.getItems().stream().mapToInt(OrderItem::getQuantity).sum();
//        assertEquals(0, sum);
//    }
//
//    @Test
//    void testSumWithEmptyOrdersList() {
//        List<Order> orders = List.of();
//        int totalSum = orders.stream().flatMap(o -> o.getItems().stream()).mapToInt(OrderItem::getQuantity).sum();
//        assertEquals(0, totalSum);
//    }
//
//    @Test
//    void testSumWithNegativeAndZeroQuantitiesAndPrices() {
//        OrderItem item1 = new OrderItem(UUID.randomUUID(), -2, 5.0);
//        OrderItem item2 = new OrderItem(UUID.randomUUID(), 0, 7.5);
//        OrderItem item3 = new OrderItem(UUID.randomUUID(), 3, -8.0);
//        Order order = new Order("T5", UUID.randomUUID(), List.of(item1, item2, item3), System.currentTimeMillis(), OrderStatus.PAID, "cashier5", 0L, 0L, 0L, "");
//        int sumQty = order.getItems().stream().mapToInt(OrderItem::getQuantity).sum();
//        double sumPrice = order.getItems().stream().mapToDouble(i -> i.getQuantity() * i.getPriceAtOrder()).sum();
//        assertEquals(1, sumQty);
//        assertEquals(-2*5.0 + 0*7.5 + 3*(-8.0), sumPrice, 0.0001);
//    }
} 
package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.order.*;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.order.OrderListResponse;
import com.cs3332.core.response.object.order.OrderResponse;
import com.cs3332.data.object.auth.UserInformation;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderItem;
import com.cs3332.data.object.order.OrderStatus;
import com.cs3332.data.object.storage.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderHandlerTest {

    @Mock
    private Server server;
    @Mock
    private UserInformation mockUser;
    @Mock
    private Product mockProduct;

    private CreateOrderHandler createOrderHandler;
    private GetOrderInfoHandler getOrderInfoHandler;
    private ListOrdersHandler listOrdersHandler;
    private UpdateOrderStatusHandler updateOrderStatusHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createOrderHandler = new CreateOrderHandler(server);
        getOrderInfoHandler = new GetOrderInfoHandler(server);
        listOrdersHandler = new ListOrdersHandler(server);
        updateOrderStatusHandler = new UpdateOrderStatusHandler(server);
    }

    @Test
    void testCreateOrder_Success() {
        // Arrange
        CreateOrderPayload payload = new CreateOrderPayload();
        List<OrderItemPayload> items = new ArrayList<>();
        items.add(new OrderItemPayload(UUID.randomUUID(), 2));
        payload.setItems(items);

        when(server.getDataManager().getAccountInformation(any())).thenReturn(mockUser);
        when(server.getDataManager().getRole(any())).thenReturn(List.of(Role.CASHIER));
        when(server.getDataManager().getProductionDBSource().getProduct(any())).thenReturn(mockProduct);
        when(mockProduct.getPrice()).thenReturn(10.0);
        when(server.getDataManager().getProductionDBSource().createOrder(any())).thenReturn(new Order());

        // Act
        ServerResponse response = createOrderHandler.resolve();

        // Assert
        assertEquals(ResponseCode.CREATED, response.getCode());
        assertTrue(response.getResponse() instanceof OrderResponse);
    }

    @Test
    void testCreateOrder_Unauthorized() {
        // Arrange
        when(server.getDataManager().getAccountInformation(any())).thenReturn(null);

        // Act
        ServerResponse response = createOrderHandler.resolve();

        // Assert
        assertEquals(ResponseCode.UNAUTHORIZED, response.getCode());
        assertTrue(response.getResponse() instanceof ErrorResponse);
    }

    @Test
    void testGetOrderInfo_Success() {
        // Arrange
        OrderInfoPayload payload = new OrderInfoPayload();
        payload.setOrderID(UUID.randomUUID());

        Order mockOrder = new Order();
        when(server.getDataManager().getProductionDBSource().getOrder(any())).thenReturn(mockOrder);

        // Act
        ServerResponse response = getOrderInfoHandler.resolve();

        // Assert
        assertEquals(ResponseCode.FOUND, response.getCode());
        assertTrue(response.getResponse() instanceof OrderResponse);
    }

    @Test
    void testGetOrderInfo_NotFound() {
        // Arrange
        OrderInfoPayload payload = new OrderInfoPayload();
        payload.setOrderID(UUID.randomUUID());

        when(server.getDataManager().getProductionDBSource().getOrder(any())).thenReturn(null);

        // Act
        ServerResponse response = getOrderInfoHandler.resolve();

        // Assert
        assertEquals(ResponseCode.NOT_FOUND, response.getCode());
        assertTrue(response.getResponse() instanceof ErrorResponse);
    }

    @Test
    void testListOrders_Success() {
        // Arrange
        ListOrdersPayload payload = new ListOrdersPayload();
        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order());

        when(server.getDataManager().getAccountInformation(any())).thenReturn(mockUser);
        when(server.getDataManager().getRole(any())).thenReturn(List.of(Role.CASHIER));
        when(server.getDataManager().getProductionDBSource().getAllOrders()).thenReturn(mockOrders);

        // Act
        ServerResponse response = listOrdersHandler.resolve();

        // Assert
        assertEquals(ResponseCode.FOUND, response.getCode());
        assertTrue(response.getResponse() instanceof OrderListResponse);
    }

    @Test
    void testListOrders_Empty() {
        // Arrange
        ListOrdersPayload payload = new ListOrdersPayload();
        when(server.getDataManager().getAccountInformation(any())).thenReturn(mockUser);
        when(server.getDataManager().getRole(any())).thenReturn(List.of(Role.CASHIER));
        when(server.getDataManager().getProductionDBSource().getAllOrders()).thenReturn(new ArrayList<>());

        // Act
        ServerResponse response = listOrdersHandler.resolve();

        // Assert
        assertEquals(ResponseCode.FOUND, response.getCode());
        assertTrue(response.getResponse() instanceof OrderListResponse);
        OrderListResponse orderListResponse = (OrderListResponse) response.getResponse();
        assertTrue(orderListResponse.getOrders().isEmpty());
    }

    @Test
    void testListOrders_Unauthorized() {
        // Arrange
        when(server.getDataManager().getAccountInformation(any())).thenReturn(null);

        // Act
        ServerResponse response = listOrdersHandler.resolve();

        // Assert
        assertEquals(ResponseCode.UNAUTHORIZED, response.getCode());
        assertTrue(response.getResponse() instanceof ErrorResponse);
    }

    @Test
    void testListOrders_InvalidRole() {
        // Arrange
        when(server.getDataManager().getAccountInformation(any())).thenReturn(mockUser);
        when(server.getDataManager().getRole(any())).thenReturn(List.of(Role.USER));

        // Act
        ServerResponse response = listOrdersHandler.resolve();

        // Assert
        assertEquals(ResponseCode.UNAUTHORIZED, response.getCode());
        assertTrue(response.getResponse() instanceof ErrorResponse);
    }

    @Test
    void testUpdateOrderStatus_Success() {
        // Arrange
        UpdateOrderStatusPayload payload = new UpdateOrderStatusPayload();
        payload.setOrderID(UUID.randomUUID());
        payload.setNewStatus(OrderStatus.PAID);

        Order mockOrder = new Order();
        mockOrder.setStatus(OrderStatus.PENDING_PAYMENT);
        when(server.getDataManager().getProductionDBSource().getOrder(any())).thenReturn(mockOrder);
        when(server.getDataManager().getProductionDBSource().updateOrderStatus(any(), any(), any()))
                .thenReturn(new com.cs3332.core.utils.Response());

        // Act
        ServerResponse response = updateOrderStatusHandler.resolve();

        // Assert
        assertEquals(ResponseCode.OK, response.getCode());
    }

    @Test
    void testUpdateOrderStatus_InvalidTransition() {
        // Arrange
        UpdateOrderStatusPayload payload = new UpdateOrderStatusPayload();
        payload.setOrderID(UUID.randomUUID());
        payload.setNewStatus(OrderStatus.PENDING_PAYMENT);

        Order mockOrder = new Order();
        mockOrder.setStatus(OrderStatus.PAID);
        when(server.getDataManager().getProductionDBSource().getOrder(any())).thenReturn(mockOrder);

        // Act
        ServerResponse response = updateOrderStatusHandler.resolve();

        // Assert
        assertEquals(ResponseCode.BAD_REQUEST, response.getCode());
        assertTrue(response.getResponse() instanceof ErrorResponse);
    }
} 
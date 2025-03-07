package com.example.MiniProject1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.model.Order;
import com.example.model.Product;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import com.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest{
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private UserRepository userRepository;

    private UUID userId;
    private UUID orderId;
    private Order testOrder;
    private Product testProduct;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository,userRepository);
        userId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        testProduct = new Product(UUID.randomUUID(), "Test Product", 10.0);
        testOrder = new Order(orderId, userId, 10.0, List.of(testProduct));
    }

    @Test
    void testMockingWorks() {
        assertNotNull(orderRepository, "OrderRepository should not be null");
        assertTrue(mockingDetails(orderRepository).isMock(), "OrderRepository should be a Mockito mock");
    }

    // Test Add Order 3 Tests
    @Test
    void testAddOrder_Positive() {
        Product product = new Product(UUID.randomUUID(), "Test Product", 10.0);
        Order order = new Order(UUID.randomUUID(), userId, 10.0, List.of(product));
        orderService.addOrder(order);
        verify(orderRepository, times(1)).addOrder(order);


    }
    @Test
    void testAddOrder_NegativeNullUserId() {
        Product product = new Product(UUID.randomUUID(), "Test Product", 10.0);
        Order order = new Order(UUID.randomUUID(), null, 10.0, List.of(product));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.addOrder(order);
        });

        assertEquals("User ID is required.", exception.getMessage());
    }
    @Test
    void testAddOrder_NegativePrice(){
        Product product = new Product(UUID.randomUUID(), "Test Product", -10.0);
        Order order = new Order(UUID.randomUUID(), userId, -10.0, List.of(product));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.addOrder(order);
        });
        assertEquals("Total price cannot be negative.", exception.getMessage());
    }

    // Test Get All Orders 3 Tests
    @Test
    void testGetOrders_Positive(){
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(testOrder);
        when(orderRepository.getOrders()).thenReturn(orders);
        List<Order> result = orderService.getOrders();
        verify(orderRepository, times(1)).getOrders();
        assertNotNull(result, "List of orders should not be null");
        assertEquals(orders.size(), result.size(), "List of orders should have the same size");
    }

    @Test
    void testGetOrders_Negative(){
        when(orderRepository.getOrders()).thenReturn(new ArrayList<>());
        List<Order> result = orderService.getOrders();
        verify(orderRepository, times(1)).getOrders();
        assertNotNull(result, "List of orders should not be null");
        assertEquals(0, result.size(), "List of orders should be empty");
    }

    @Test
    void testGetOrders_EdgeNull(){ //CHECK WITH ZIAD LAW IT SHOULD THROW EXCEPTION WALA LA2.
        when(orderRepository.getOrders()).thenReturn(null);
        Exception exception = assertThrows(NullPointerException.class, () -> {
            orderService.getOrders();
        });

        assertNotNull(exception, "NullPointerException should be thrown");
    }

    // Test Get Order By ID 3 Tests
    @Test
    void testGetOrderById_Positive(){
        when(orderRepository.getOrderById(orderId)).thenReturn(testOrder);
        Order result = orderService.getOrderById(orderId);
        verify(orderRepository, times(1)).getOrderById(orderId);
        assertNotNull(result, "Order should not be null");
        assertEquals(testOrder, result, "Order should be the same");
    }

    @Test
    void testGetOrderById_Negative(){
        when(orderRepository.getOrderById(orderId)).thenReturn(null);
        Order result = orderService.getOrderById(orderId);
        verify(orderRepository, times(1)).getOrderById(orderId);
        assertNull(result, "Order should be null");
    }

    void testGetOrderById_NullNoException(){


    }

    // Test Delete Order By ID 3 Tests
    @Test
    void testDeleteOrderById_Positive(){
        when(orderRepository.getOrderById(orderId)).thenReturn(testOrder);
        orderService.deleteOrderById(orderId);
        verify(orderRepository, times(1)).deleteOrderById(orderId);
    }

    @Test
    void testDeleteOrderById_Negative(){
        when(orderRepository.getOrderById(orderId)).thenReturn(null);
        orderService.deleteOrderById(orderId);
        verify(orderRepository, times(0)).deleteOrderById(orderId);
    }









}
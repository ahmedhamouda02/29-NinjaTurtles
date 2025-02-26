package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings("rawtypes")
public class OrderService extends MainService<Order> {

    private final OrderRepository orderRepository;

    // Constructor Injection
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Add an order
    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    // Get all orders
    public ArrayList<Order> getOrders() {
        return orderRepository.getOrders();
    }

    // Get specific order
    public Order getOrderById(UUID orderId) {
        return orderRepository.getOrderById(orderId);
    }

    // Delete specific order
    public void deleteOrderById(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found!");
        }
        orderRepository.deleteOrderById(orderId);
    }
}

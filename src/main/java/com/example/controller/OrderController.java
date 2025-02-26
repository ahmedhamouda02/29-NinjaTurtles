package com.example.controller;

import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    // Constructor Injection
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Add a new order
    @PostMapping("/")
    public void addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
    }

    // Get a specific order
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId);
    }

    // Get all orders
    @GetMapping("/")
    public ArrayList<Order> getOrders() {
        return orderService.getOrders();
    }

    // Delete a specific order
    @DeleteMapping("/delete/{orderId}")
    public String deleteOrderById(@PathVariable UUID orderId) {
        try {
            orderService.deleteOrderById(orderId);
            return "Order deleted successfully.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }
}

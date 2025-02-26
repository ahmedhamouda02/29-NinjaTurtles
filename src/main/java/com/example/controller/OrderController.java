package com.example.controller;

import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  // Add a new order
  @PostMapping("/")
  public ResponseEntity<?> addOrder(@RequestBody Order order) {
    try {
      orderService.addOrder(order);
      return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // Get a specific order
  @GetMapping("/{orderId}")
  public ResponseEntity<?> getOrderById(@PathVariable UUID orderId) {
    try {
      Order order = orderService.getOrderById(orderId);
      return ResponseEntity.ok(order);
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found: " + orderId);
    }
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

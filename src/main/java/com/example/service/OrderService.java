package com.example.service;

import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings("rawtypes")
public class OrderService extends MainService<Order> {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;

  public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
  }

  // Add an order
  public void addOrder(Order order) {
    if (order.getUserId() == null) {
      throw new IllegalArgumentException("User ID is required.");
    }

    // if (userRepository.getUserById(order.getUserId()) == null) {
    // userRepository.addUser(new User());
    // }

    // if (order.getProducts() == null || order.getProducts().isEmpty()) {
    // throw new IllegalArgumentException("Order must contain at least one
    // product.");
    // }

    if (order.getTotalPrice() < 0) {
      throw new IllegalArgumentException("Total price cannot be negative.");
    }

    for (Product product : order.getProducts()) {
      if (product.getId() == null || product.getName() == null || product.getPrice() < 0) {
        throw new IllegalArgumentException("Each product must have a valid ID, name, and price.");
      }
    }

    UUID orderId = order.getId() != null ? order.getId() : UUID.randomUUID();
    order.setId(orderId);
    orderRepository.addOrder(order);
  }

  // Get all orders
  public ArrayList<Order> getOrders() {
    return orderRepository.getOrders();
  }

  // Get specific order
  public Order getOrderById(UUID orderId) {
    // Order order = orderRepository.getOrderById(orderId);

    // if (order == null) {
    // throw new NoSuchElementException("Order with ID " + orderId + " not found.");
    // }

    // return order;
    return orderRepository.getOrderById(orderId);

  }

  // Delete specific order
  public void deleteOrderById(UUID orderId) {
    Order order = orderRepository.getOrderById(orderId);
    if (order == null) {
      return;
    }
    orderRepository.deleteOrderById(orderId);
  }

}

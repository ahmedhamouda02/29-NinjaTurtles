package com.example.repository;

import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository extends MainRepository<Order> {

  @Override
  protected String getDataPath() {
    return "src/main/java/com/example/data/orders.json";
  }

  @Override
  protected Class<Order[]> getArrayType() {
    return Order[].class;
  }

  public void addOrder(Order order) {
    if (order.getId() == null) {
      order.setId(UUID.randomUUID());
    }
    save(order);
  }

  public ArrayList<Order> getOrders() {
    return findAll();
  }

  public Order getOrderById(UUID orderId) {
    return findAll().stream()
        .filter(order -> order.getId().equals(orderId))
        .findFirst()
        .orElse(null);
  }

  public void deleteOrderById(UUID orderId) {
    ArrayList<Order> orders = findAll();
    orders.removeIf(order -> order.getId().equals(orderId));
    overrideData(orders);
  }
}

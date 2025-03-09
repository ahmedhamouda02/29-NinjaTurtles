package com.example.repository;

import com.example.model.User;
import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepository extends MainRepository<User> {
  String usersJsonPath = System.getenv("USERS_JSON_PATH");

  @Override
  protected String getDataPath() {
    if (usersJsonPath == null || usersJsonPath.isEmpty())
      return "src/main/java/com/example/data/users.json";
    return usersJsonPath;
  }

  @Override
  protected Class<User[]> getArrayType() {
    return User[].class;
  }

  public ArrayList<User> getUsers() {
    return findAll();
  }

  public User getUserById(UUID userId) {
    return findAll().stream()
        .filter(user -> user.getId().equals(userId))
        .findFirst()
        .orElse(null);
  }

  public User addUser(User user) {
    ArrayList<User> users = findAll();
    users.add(user);
    saveAll(users);
    return user;
  }

  public List<Order> getOrdersByUserId(UUID userId) {
    User user = getUserById(userId);
    return (user != null) ? user.getOrders() : new ArrayList<>();
  }

  public void addOrderToUser(UUID userId, Order order) {
    User user = getUserById(userId);
    if (user != null) {
      user.getOrders().add(order);
      save(user);
    }
  }

  public void removeOrderFromUser(UUID userId, UUID orderId) {
    User user = getUserById(userId);
    user.getOrders().removeIf(order -> order.getId().equals(orderId));
    save(user);
  }

  public void deleteUserById(UUID userId) {
    ArrayList<User> users = findAll();
    users.removeIf(user -> user.getId().equals(userId));
    saveAll(users);
  }
}

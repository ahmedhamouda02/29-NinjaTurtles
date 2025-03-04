package com.example.repository;

import com.example.model.User;
import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepository extends MainRepository<User> {

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/users.json";
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }

    // Retrieve all users
    public ArrayList<User> getUsers() {
        return findAll();
    }

    // Retrieve a user by ID
    public User getUserById(UUID userId) {
        return findAll().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // Add a new user
    public User addUser(User user) {
        ArrayList<User> users = findAll();
        users.add(user);
        saveAll(users);
        return user;
    }

    // Retrieve all orders for a user
    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        return (user != null) ? user.getOrders() : new ArrayList<>();
    }

//    // Add an order to a specific user
    public void addOrderToUser(UUID userId, Order order) {
        User user = getUserById(userId);
        if (user != null) {
            user.getOrders().add(order);
            save(user);
        }
    }

    // Remove an order from a user
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        User user = getUserById(userId);
        user.getOrders().removeIf(order -> order.getId().equals(orderId));
        save(user);
    }


    // Delete a user by ID
    public void deleteUserById(UUID userId) {
        ArrayList<User> users = findAll();
        users.removeIf(user -> user.getId().equals(userId));
        saveAll(users);
    }
}

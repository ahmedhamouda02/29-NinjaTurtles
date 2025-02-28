package com.example.service;

import com.example.model.User;
import com.example.model.Order;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Constructor with Dependency Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Add a new user
    public User addUser(User user) {
        ArrayList<User> users = userRepository.getUsers();

        validateUser(user, users);

        for (Order order : user.getOrders()) {
            validateOrder(order, user);
        }

        user.setId(UUID.randomUUID());
        users.add(user);
        userRepository.saveAll(users);
        return user;
    }

    private void validateUser(User user, ArrayList<User> users) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name should not be empty.");
        }
        if (user.getOrders() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orders cannot be null. Use an empty list instead.");
        }

        boolean userExists = users.stream()
                .anyMatch(existingUser -> existingUser.getId().equals(user.getId()));

        if (userExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists with the same ID.");
        }
    }

    private void validateOrder(Order order, User user) {
        if (order.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each order must be associated with a user.");
        }
        if (!order.getUserId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order user ID " + order.getUserId() + " does not match the given user ID " + user.getId() + ".");
        }
        if (order.getTotalPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order total price cannot be negative.");
        }
        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each order must contain at least one product.");
        }
    }

    // Get all users
    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    // Get user by ID
    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    // Get all orders for a user
    public List<Order> getOrdersByUserId(UUID userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found.");
        }
        return userRepository.getOrdersByUserId(userId);
    }

    // Add an order to a user


//    public void addOrderToUser(UUID userId, Order order) {
//        if (userId == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be null.");
//        }
//        if (order == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order cannot be null.");
//        }
//
//        User user = userRepository.getUserById(userId);
//        if (user == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found.");
//        }
//
//        if (user.getOrders() == null) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User's order list is not initialized.");
//        }
//
//        if (user.getOrders().contains(order)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is already associated with this user.");
//        }
//
//        userRepository.addOrderToUser(userId, order);
//    }

    // Empty the cart (removes all orders from a user's order list)
    public void emptyCart(UUID userId) {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            user.setOrders(new ArrayList<>());
            userRepository.saveAll(userRepository.getUsers());
        }
    }

    // Remove an order from a user
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be null.");
        }
        if (orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID cannot be null.");
        }

        User user = userRepository.getUserById(userId);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found.");
        }

        if (user.getOrders() == null || user.getOrders().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has no orders to remove.");
        }

        boolean orderExists = user.getOrders().stream()
                .anyMatch(order -> order.getId().equals(orderId));

        if (!orderExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + orderId + " not found for this user.");
        }

        userRepository.removeOrderFromUser(userId, orderId);
    }


    // Delete a user by ID
    public void deleteUserById(UUID userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found.");
        }
        userRepository.deleteUserById(userId);
    }
}

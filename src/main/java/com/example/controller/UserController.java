package com.example.controller;

import com.example.model.User;
import com.example.model.Order;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Constructor with Dependency Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Add a new user
    @PostMapping("/")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            User newUser = userService.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Get all users
    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    // Get orders of a specific user
    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable UUID userId) {
        try {
            List<Order> orders = userService.getOrdersByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Checkout and add orders to user (assuming some checkout process)
    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId, @RequestBody Order order) {
        userService.addOrderToUser(userId, order);
        return "Order added successfully!";
    }

    // Remove an order from a user
    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {
        userService.removeOrderFromUser(userId, orderId);
        return "Order removed successfully!";
    }

    // Empty a user's cart
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        userService.emptyCart(userId);
        return "Cart emptied successfully!";
    }

    // Add a product to a user's cart (assuming a ProductService handles product-related operations)
    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        return "Product added to cart!";
    }

    // Remove a product from a user's cart
    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        return "Product removed from cart!";
    }

    // Delete a user by ID
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable UUID userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User deleted successfully!");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}

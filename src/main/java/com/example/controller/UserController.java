package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.Order;
import com.example.service.CartService;
import com.example.service.ProductService;
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

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Constructor with Dependency Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Add a new user
    @PostMapping("/")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            User newUser = userService.addUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(newUser);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Get all users
    @GetMapping("/")
    public ResponseEntity<?> getUsers() {
        try {
            ArrayList<User> users = userService.getUsers();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
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
    public ResponseEntity<?> addOrderToUser(@PathVariable UUID userId, @RequestBody Order order) {
        try {
            userService.addOrderToUser(userId, order);
            return ResponseEntity.ok("Order added successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Remove an order from a user
    @PostMapping("/{userId}/removeOrder")
    public ResponseEntity<String> removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {
        try {
            userService.removeOrderFromUser(userId, orderId);
            return ResponseEntity.ok("Order removed successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    // Empty a user's cart
    @DeleteMapping("/{userId}/emptyCart")
    public ResponseEntity<String> emptyCart(@PathVariable UUID userId) {
        try {
            userService.emptyCart(userId);
            return ResponseEntity.ok("Cart emptied successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Add a product to a user's cart (assuming a ProductService handles product-related operations)
    @PutMapping("/addProductToCart")
    public ResponseEntity<String> addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        try {
            System.out.println("in addProductToCart API in controller");
            Product product = productService.getProductById(productId);
            System.out.println("Product name: " + product.getName());
            cartService.addProductToCart(userId, product);
            System.out.println("Product added to cart");
            return ResponseEntity.ok("Product added to cart");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Remove a product from a user's cart
    @PutMapping("/deleteProductFromCart")
    public ResponseEntity<String> deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        try {
            Product product = productService.getProductById(productId);
            System.out.println("Product name: " + product.getName());
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
            System.out.println("Product found");
            Cart cart = cartService.getCartByUserId(userId);
            System.out.println("Cart ID" + cart.getId());
            if (cart == null || cart.getProducts().isEmpty()) {
                return ResponseEntity.ok("Cart is empty");
            }
            System.out.println("Cart found");
            cartService.deleteProductFromCart(cart.getId(), product);
            System.out.println("Product deleted from cart");
            return ResponseEntity.ok("Product deleted from cart");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // Delete a user by ID
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable UUID userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}

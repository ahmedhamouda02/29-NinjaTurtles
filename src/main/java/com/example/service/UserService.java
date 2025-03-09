package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
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

  @Autowired
  private OrderService orderService;

  @Autowired
  private ProductService productService;

  @Autowired
  private CartService cartService;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User addUser(User user) {
    ArrayList<User> users = userRepository.getUsers();

    validateUser(user, users);

    for (Order order : user.getOrders()) {
      validateOrder(order, user);
    }

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

  public ArrayList<User> getUsers() {
    ArrayList<User> users = userRepository.getUsers();

    if (users == null || users.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Users not found");
    }

    return users;
  }

  public User getUserById(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User not found");
    }
    return getUserByIdOrThrow(userId);
  }

  public List<Order> getOrdersByUserId(UUID userId) {
    getUserByIdOrThrow(userId);
    return userRepository.getOrdersByUserId(userId);
  }

  public void addOrderToUser(UUID userId) {
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be null.");
    }

    User user = getUserByIdOrThrow(userId);
    System.out.println("validated user");
    Cart cart = getCartByUserOrThrow(userId);
    System.out.println("validated cart");
    double totalPrice = calculateTotalPrice(cart);
    System.out.println("calculated price");
    Order newOrder = createNewOrder(userId, cart, totalPrice);
    System.out.println("created new order");
    orderService.addOrder(newOrder);
    System.out.println("added order to json through order service");
    userRepository.addOrderToUser(userId, newOrder);
    System.out.println("added order to user");
    emptyUserCart(cart);
    System.out.println("empty cart");
  }

  private Cart getCartByUserOrThrow(UUID userId) {
    System.out.println("in cart or throw");
    Cart cart = cartService.getCartByUserId(userId);
    System.out.println("got cart by user id");
    if (cart == null || cart.getProducts().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot create an order. The cart is empty.");
    }
    return cart;
  }

  private double calculateTotalPrice(Cart cart) {
    return cart.getProducts().stream()
        .mapToDouble(Product::getPrice)
        .sum();
  }

  private Order createNewOrder(UUID userId, Cart cart, double totalPrice) {
    return new Order(UUID.randomUUID(), userId, totalPrice, new ArrayList<>(cart.getProducts()));
  }

  private void emptyUserCart(Cart cart) {
    cartService.deleteCartById(cart.getId());
  }

  public void emptyCart(UUID userId) {
    User user = getUserByIdOrThrow(userId);
    Cart cart = getCartByUserOrThrow(userId);
    clearCartProducts(cart);
  }

  private void clearCartProducts(Cart cart) {
    if (!cart.getProducts().isEmpty()) {
      for (Product product : cart.getProducts()) {
        cartService.deleteProductFromCart(cart.getId(), product);
      }
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No products found");
    }
  }

  public void removeOrderFromUser(UUID userId, UUID orderId) {
    if (userId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be null.");
    }
    if (orderId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID cannot be null.");
    }

    User user = getUserByIdOrThrow(userId);

    if (user.getOrders() == null || user.getOrders().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has no orders to remove.");
    }

    boolean orderExists = user.getOrders().stream()
        .anyMatch(order -> order.getId().equals(orderId));

    if (!orderExists) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
    }

    userRepository.removeOrderFromUser(userId, orderId);
  }

  public void deleteUserById(UUID userId) {
    getUserByIdOrThrow(userId);
    userRepository.deleteUserById(userId);
  }

  private User getUserByIdOrThrow(UUID userId) {
    User user = userRepository.getUserById(userId);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.OK, "User not found");
    }
    return user;
  }
}

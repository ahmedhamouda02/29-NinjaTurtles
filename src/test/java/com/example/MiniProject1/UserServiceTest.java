package com.example.MiniProject1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;
    private User testUser;
    private UUID userId;
    private UUID orderId;
    @Autowired
    private User user;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        testUser = new User(userId, "HamoElTikha", new ArrayList<>());
    }
    // TEST EL ADD USER 3 TESTS 1 POSITIVE 1 NEGATIVE 1 EDGE
    @Test
    void testAddUser_Positive(){
        when(userRepository.getUsers()).thenReturn(new ArrayList<>());
        User result = userService.addUser(testUser);
        assertNotNull(result, "User should not be null");
        assertEquals(testUser.getName(), result.getName(), "User should have the same name");
        verify(userRepository, times(1)).saveAll(any());
    }

    @Test
    void testAddUser_Negative(){

        ArrayList<User> existingUsers = new ArrayList<>();
        existingUsers.add(testUser); // Existing user with same ID

        when(userRepository.getUsers()).thenReturn(existingUsers);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.addUser(testUser);
        });

        assertEquals("400 BAD_REQUEST \"User already exists with the same ID.\"", exception.getMessage());
    }

    @Test
    void testAddUser_NegativeNull(){
        testUser.setName(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.addUser(testUser), "User should not be null");
        assertEquals("400 BAD_REQUEST \"User name should not be empty.\"", exception.getMessage());
    }

    // TEST LEL getUsers() 3 tests
    @Test
    void testGetUsers(){
        ArrayList<User> users = new ArrayList<>();
        users.add(testUser);
        when(userRepository.getUsers()).thenReturn(users);
        List<User> result = userService.getUsers();
        assertNotNull(result, "Users should not be null");
        assertEquals(users, result, "Users should be the same");
    }

    @Test
    void testGetUsers_Negative(){
        when(userRepository.getUsers()).thenReturn(new ArrayList<>());
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.getUsers(), "Users should not be null");
        assertEquals("404 NOT_FOUND \"Users not found\"", exception.getMessage());
    }

    @Test
    void testGetUsers_Edge(){
        when(userRepository.getUsers()).thenReturn(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.getUsers(), "Users should not be null");
        assertEquals("404 NOT_FOUND \"Users not found\"", exception.getMessage());
    }

    // TEST LEL GET USER BY ID 3 TESTS
    @Test
    void testGetUserById_Positive(){
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        User result = userService.getUserById(userId);
        assertNotNull(result, "User should not be null");
        assertEquals(testUser.getName(), result.getName(), "User should have the same name");
    }

    @Test
    void testGetUserById_Negative(){
        when(userRepository.getUserById(userId)).thenReturn(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.getUserById(userId), "User should exist");
        assertEquals("200 OK \"User not found\"", exception.getMessage());
    }

    @Test
    void testGetUserById_Edge(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(null);  // Passing null user ID
        });

        assertEquals("User not found", exception.getMessage(), "Exception should be thrown for null ID");
    }

    // Test getOrdersByUserId 3 tests
    @Test
    void testGetOrdersByUserId_Positive(){
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        List<Order> orders = List.of(new Order(UUID.randomUUID(), userId, 100.0, new ArrayList<>()));

        when(userRepository.getOrdersByUserId(userId)).thenReturn(orders);

        List<Order> result = userService.getOrdersByUserId(userId);

        assertNotNull(result, "Orders should not be null");
        assertFalse(result.isEmpty(), "Order list should not be empty");
        assertEquals(1, result.size(), "User should have 1 order");
    }

    @Test
    void testGetOrdersByUserId_Negative(){
        when(userRepository.getOrdersByUserId(userId)).thenReturn(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.getOrdersByUserId(userId), "Orders should not be null");
        assertEquals("200 OK \"User not found\"", exception.getMessage());
    }

    @Test
    void testGetOrderByUserId_EdgeNoOrder(){
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        when(userRepository.getOrdersByUserId(userId)).thenReturn(new ArrayList<>());
        List<Order> result = userService.getOrdersByUserId(userId);
        assertNotNull(result, "Orders should not be null");
        assertTrue(result.isEmpty(), "Order list should be empty");
    }

    // Test addOrderToUser 3 tests
    @Test
    void testAddOrderToUser_Positive(){
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        Product product = new Product(UUID.randomUUID(), "Test Hamo Product", 100.0);
        Cart cart = new Cart(UUID.randomUUID(), userId, List.of(product));
        when(cartService.getCartByUserId(userId)).thenReturn(cart);
        Order order = new Order(UUID.randomUUID(), userId, 100.0, List.of(product));
        doNothing().when(orderService).addOrder(any(Order.class));
        userService.addOrderToUser(userId);
        verify(userRepository, times(1)).addOrderToUser(eq(userId), any(Order.class));
        verify(cartService, times(1)).deleteCartById(cart.getId());
        verify(orderService, times(1)).addOrder(any(Order.class));
    }

    @Test
    void testAddOrderToUser_Negative(){
        when(userRepository.getUserById(userId)).thenReturn(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.addOrderToUser(userId), "User should exist");
        assertEquals("200 OK \"User not found\"", exception.getMessage());

    }

    @Test
    void testAddOrderToUser_EmptyCart(){
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        when(cartService.getCartByUserId(userId)).thenReturn(cart);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.addOrderToUser(userId), "Cart should not be empty");
        assertEquals("400 BAD_REQUEST \"Cannot create an order. The cart is empty.\"", exception.getMessage());
    }

    // Empty Cart Tests
    @Test
    void testEmptyCart_Positive() {
        when(userRepository.getUserById(userId)).thenReturn(testUser);

        Product product = new Product(UUID.randomUUID(), "Test Hamo Product", 10.0);
        Cart cart = new Cart(UUID.randomUUID(), userId, List.of(product));
        when(cartService.getCartByUserId(userId)).thenReturn(cart);

        doNothing().when(cartService).deleteProductFromCart(any(UUID.class), any(Product.class));

        userService.emptyCart(userId);

        verify(cartService, times(1)).deleteProductFromCart(eq(cart.getId()), any(Product.class));
    }
    @Test
    void testEmptyCart_NegativeUserNotFound(){
        when(userRepository.getUserById(userId)).thenReturn(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.emptyCart(userId), "User should exist");
        assertEquals("200 OK \"User not found\"", exception.getMessage());

    }
    @Test
    void testEmptyCart_EdgeEmpty(){
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        when(cartService.getCartByUserId(userId)).thenReturn(cart);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.emptyCart(userId), "Cart should not be empty");
        assertEquals("404 NOT_FOUND \"No products found\"", exception.getMessage());
    }

    // Remove Order from User 3 Tests
    @Test
    void testRemoveOrderFromUser_Positive(){
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        Order order = new Order(UUID.randomUUID(), userId, 100.0, new ArrayList<>());
        testUser.setOrders(List.of(order));
        doNothing().when(userRepository).removeOrderFromUser(userId, order.getId());
        userService.removeOrderFromUser(userId, order.getId());
        verify(userRepository, times(1)).removeOrderFromUser(userId, order.getId());
    }
    @Test
    void testRemoveOrderFromUser_NegativeOrderNotFound(){
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        Order order = new Order(UUID.randomUUID(), userId, 100.0, new ArrayList<>());
        testUser.setOrders(List.of(order));
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.removeOrderFromUser(userId, UUID.randomUUID()), "Order should exist");
        assertEquals("404 NOT_FOUND \"Order not found\"", exception.getMessage());
    }

    @Test
    void testRemoveOrderFromUser_EdgeNoOrders() {
        when(userRepository.getUserById(userId)).thenReturn(testUser); //
        testUser.setOrders(null);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.removeOrderFromUser(userId, orderId);
        });

        assertEquals("400 BAD_REQUEST \"User has no orders to remove.\"", exception.getMessage());
    }

    // Delete User By ID 3 Tests
    @Test
    void testDeleteUserById_Positive(){
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        doNothing().when(userRepository).deleteUserById(userId);
        userService.deleteUserById(userId);
        verify(userRepository, times(1)).deleteUserById(userId);
    }

    @Test
    void testDeleteUserById_Negative(){
        when(userRepository.getUserById(userId)).thenReturn(null);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userService.deleteUserById(userId), "User should exist");
        assertEquals("200 OK \"User not found\"", exception.getMessage());
    }

    @Test
    void testDeleteUserById_EdgeNullUser(){
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.deleteUserById(null);  // Passing null user ID
        });

        assertEquals("200 OK \"User not found\"", exception.getMessage());
    }







}
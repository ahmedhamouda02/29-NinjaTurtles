package com.example.MiniProject1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CartServiceTest {
    @InjectMocks
    private CartService cartService;
    @Mock
    private CartRepository cartRepository;

    private Cart cartTest;
    private Product productTest;
    private UUID cartId;
    private UUID userId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cartId = UUID.randomUUID();
        userId = UUID.randomUUID();
        productTest = new Product(UUID.randomUUID(), "Test Product", 10.0);
        cartTest = new Cart(cartId, userId, new ArrayList<Product>());
    }

    // Awel Test Haykon Positive Test.

    // ADD CART TESTS! 3 tests, 1 positive w 1 negative w 1 edge case
    @Test
    public void testAddCart_Positive(){
        when(cartRepository.addCart(cartTest)).thenReturn(cartTest);
        cartTest.getProducts().add(productTest);
        Cart result = cartService.addCart(cartTest);
        assertNotNull(result, "Cart should not be null");
        assertEquals(cartTest.getUserId(), result.getUserId(), "Cart should have the same user ID");
    }

    @Test
    public void testAddCart_Negative(){
        cartTest.setUserId(null);
        assertThrows(IllegalArgumentException.class, () -> cartService.addCart(cartTest), "Cart should have a user ID");
    }

    @Test
    public void testAddCart_EmptyProductsAccept() { // mustaccept
        cartTest.setProducts(new ArrayList<>());
        when(cartRepository.addCart(cartTest)).thenReturn(cartTest);
        Cart result = cartService.addCart(cartTest);
        assertNotNull(result, "Cart created even with no products.");
        assertEquals(0, result.getProducts().size(), "Cart should have an empty product list.");



    }

    // GET CARTS TESTS! 3 tests, 1 positive w 1 negative w 1 edge case
    @Test
    public void testGetCarts_Positive(){
        List<Cart> carts = new ArrayList<>();
        carts.add(cartTest);
        when(cartRepository.getCarts()).thenReturn((ArrayList<Cart>) carts);
        List<Cart> result = cartService.getCarts();
        assertNotNull(result, "List of carts should not be null");
        assertEquals(carts.size(), result.size(), "List of carts should have the same size");
    }

    @Test
    public void testGetCarts_Negative(){
        when(cartRepository.getCarts()).thenReturn(new ArrayList<>());
        List<Cart> result = cartService.getCarts();
        assertNotNull(result, "List of carts should not be null");
        assertEquals(0, result.size(), "List of carts should be empty");
    }

    @Test
    public void testGetCarts_Edge(){
        when(cartRepository.getCarts()).thenReturn(null);
        List<Cart> result = cartService.getCarts();
        assertNull(result, "List of carts should be null");
    }

    // GET CART BY ID TESTS! 3 tests, 1 positive w 1 negative w 1 edge case
    @Test
    public void testGetCartById_Positive(){
        when(cartRepository.getCartById(cartId)).thenReturn(cartTest);
        Cart result = cartService.getCartById(cartId);
        assertNotNull(result, "Cart should not be null");
        assertEquals(cartTest.getUserId(), result.getUserId(), "Cart should have the same user ID");
    }

    @Test
    public void testGetCartById_Negative(){
        when(cartRepository.getCartById(cartId)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> cartService.getCartById(cartId), "Cart should exist");
    }

    @Test
    public void testGetCartById_Edge(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCartById(null);  // Passing null cart ID
        });

        assertEquals("Cart not found", exception.getMessage(), "Exception should be thrown for null ID");
    }

    // GET CART BY USER ID TESTS! 3 tests, 1 positive w 1 negative w 1 edge case
    @Test
    public void testGetCartByUserId_Positive(){
        when(cartRepository.getCartByUserId(userId)).thenReturn(cartTest);
        Cart result = cartService.getCartByUserId(userId);
        assertNotNull(result, "Cart should not be null");
        assertEquals(cartTest.getUserId(), result.getUserId(), "Cart should have the same user ID");
    }

    @Test
    public void testGetCartByUserId_Negative(){
        when(cartRepository.getCartByUserId(userId)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> cartService.getCartByUserId(userId), "Cart should exist");
    }

    @Test
    public void testGetCartByUserId_Edge(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCartByUserId(null);  //badeelo null user id
        });

        assertEquals("Cart not found", exception.getMessage(), "Exception should be thrown for null ID");
    }

    // ADD PRODUCT TO CART TESTS! 3 tests, 1 positive w 1 negative w 1 edge case
    @Test
    public void testAddProductToCart_Positive(){
        when(cartRepository.getCartByUserId(userId)).thenReturn(cartTest);
        cartService.addProductToCart(userId, productTest);
        verify(cartRepository, times(1)).updateCart(cartTest);  // Ensures updateCart is called
        assertTrue(cartTest.getProducts().contains(productTest), "Cart should contain the added product");
    }

    @Test
    public void testAddProductToCart_Negative() {
        when(cartRepository.getCartByUserId(userId)).thenReturn(null);
        // hay caryet cart gedida
        cartService.addProductToCart(userId, productTest);
        verify(cartRepository, times(1)).addCart(any(Cart.class));
    }

    @Test
    public void testAddProductToCart_Edge(){ // hadeelo null fel userid mafrod exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addProductToCart(null, productTest);
        });

        assertEquals("User ID cannot be null", exception.getMessage(), "Should throw an error for null user ID");
    }

    // DELETE PRODUCT FROM CART TESTS! 3 tests, 1 positive w 1 negative w 1 edge case
    @Test
    public void testDeleteProductFromCart_Positive(){
        doNothing().when(cartRepository).deleteProductFromCart(cartId,productTest );
        cartService.deleteProductFromCart(cartId, productTest);
        verify(cartRepository, times(1)).deleteProductFromCart(cartId, productTest);
    }

    @Test
    public void testDeleteProductFromCart_Negative(){
        doThrow(new RuntimeException("Cart not found")).when(cartRepository).deleteProductFromCart(cartId, productTest);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.deleteProductFromCart(cartId, productTest);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    public void testDeleteProductFromCart_Edge(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteProductFromCart(null, productTest); // mafrood exception ama adeelo null user id
        });

        assertEquals("Cart ID cannot be null", exception.getMessage(), "Should throw an error for null user ID");
    }

    // DELETE CART BY ID TESTS! 3 tests, 1 positive w 1 negative w 1 edge case
    @Test
    public void testDeleteCartById_Positive(){
        doNothing().when(cartRepository).deleteCartById(cartId);
        cartService.deleteCartById(cartId);
        verify(cartRepository, times(1)).deleteCartById(cartId);
    }

    @Test
    public void testDeleteCartById_Negative(){
        doThrow(new RuntimeException("Cart not found")).when(cartRepository).deleteCartById(cartId);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.deleteCartById(cartId);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    public void testDeleteCartById_Edge(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCartById(null); // mafrood exception ama adeelo null user id
        });

        assertEquals("Cart ID cannot be null", exception.getMessage(), "Should throw an error for null user ID");
    }





}


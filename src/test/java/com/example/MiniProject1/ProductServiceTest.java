package com.example.MiniProject1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.model.Order;
import com.example.model.Product;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import com.example.repository.ProductRepository;
import com.example.service.OrderService;
import com.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
class ProductServiceTest{
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private UUID productId;
    private Product testProduct;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
        productId = UUID.randomUUID();
        testProduct = new Product(productId, "Test Product", 10.0);
    }

    // Test Add Product
    @Test
    void testAddProduct_Positive() {
        when(productRepository.addProduct(testProduct)).thenReturn(testProduct);
        Product product = productService.addProduct(testProduct);
        assertNotNull(product, "Product should not be null");
        assertEquals(testProduct.getName(), product.getName(), "Product name should be the same");
    }

    @Test
    void testAddProduct_EmptyName(){
        testProduct.setName("");
        //response status
        assertThrows(ResponseStatusException.class, () -> productService.addProduct(testProduct));
    }

    @Test
    void testAddProduct_NegativePrice(){
        testProduct.setPrice(-10.0);
        //response status
        assertThrows(ResponseStatusException.class, () -> productService.addProduct(testProduct));
    }

    // Test Get Products
    @Test
    void testGetProducts_Positive() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(testProduct);
        when(productRepository.getProducts()).thenReturn(products);
        ArrayList<Product> result = productService.getProducts();
        assertNotNull(result, "Result should not be null");
        assertEquals(products.size(), result.size(), "Result size should be the same");
        assertEquals(products.get(0).getName(), result.get(0).getName(), "Product name should be the same");
    }

    @Test
    void testGetProducts_Empty(){
        ArrayList<Product> products = new ArrayList<>();
        when(productRepository.getProducts()).thenReturn(products);
        ArrayList<Product> result = productService.getProducts();
        assertNotNull(result, "Result should not be null");
        assertEquals(products.size(), result.size(), "Result size should be the same");
    }

    @Test
    void testGetProducts_MultipleProducts(){
        ArrayList<Product> products = new ArrayList<>();
        products.add(testProduct);
        products.add(new Product(UUID.randomUUID(), "Test Product 2", 20.0));
        when(productRepository.getProducts()).thenReturn(products);
        ArrayList<Product> result = productService.getProducts();
        assertNotNull(result, "Result should not be null");
        assertEquals(products.size(), result.size(), "Result size should be the same");
        assertEquals(products.get(0).getName(), result.get(0).getName(), "Product name should be the same");
        assertEquals(products.get(1).getName(), result.get(1).getName(), "Product name should be the same");

    }

    // Test Get Product By ID
    @Test
    void testGetProductById_Positive() {
        when(productRepository.getProductById(productId)).thenReturn(testProduct);
        Product product = productService.getProductById(productId);
        assertNotNull(product, "Product should not be null");
        assertEquals(testProduct.getName(), product.getName(), "Product name should be the same");
    }

    @Test
    void testGetProductById_Negative() {
        when(productRepository.getProductById(productId)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> productService.getProductById(productId));
    }
    @Test
    void testGetProductById_InvalidId(){
        UUID invalidId = UUID.randomUUID();
        when(productRepository.getProductById(invalidId)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> productService.getProductById(invalidId));
    }

    // Test Update Product
    @Test
    void testUpdateProduct_Positive() {
        String newName = "New Product";
        double newPrice = 20.0;
        when(productRepository.updateProduct(productId, newName, newPrice)).thenReturn(new Product(productId, newName, newPrice));
        Product product = productService.updateProduct(productId, newName, newPrice);
        assertNotNull(product, "Product should not be null");
        assertEquals(newName, product.getName(), "Product name should be the same");
        assertEquals(newPrice, product.getPrice(), "Product price should be the same");
    }

    @Test
    void testUpdateProduct_EmptyName(){
        String newName = "";
        double newPrice = 20.0;
        assertThrows(ResponseStatusException.class, () -> productService.updateProduct(productId, newName, newPrice));
    }

    @Test
    void testUpdateProduct_NegativePrice(){
        String newName = "New Product";
        double newPrice = -20.0;
        assertThrows(ResponseStatusException.class, () -> productService.updateProduct(productId, newName, newPrice));
    }

    // Apply Discount
    @Test
    void testApplyDiscount_Positive() {
        double discount = 10.0;
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(productId);
        productService.applyDiscount(discount, productIds);
        verify(productRepository, times(1)).applyDiscount(discount, productIds);
    }

    @Test
    void testApplyDiscount_Negative() {
        double discount = -10.0;
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(productId);
        assertThrows(ResponseStatusException.class, () -> productService.applyDiscount(discount, productIds));
    }

    @Test
    void testApplyDiscount_TooHigh() {
        double discount = 110.0;
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(productId);
        assertThrows(ResponseStatusException.class, () -> productService.applyDiscount(discount, productIds));
    }

    // Delete Product
    @Test
    void testDeleteProduct_Positive() {
        when(productRepository.getProductById(productId)).thenReturn(testProduct);
        productService.deleteProductById(productId);
        verify(productRepository, times(1)).deleteProductById(productId);
    }

    @Test
    void testDeleteProduct_Negative() {
        when(productRepository.getProductById(productId)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> productService.deleteProductById(productId));
    }

    @Test
    void testDeleteProduct_InvalidId() {
        UUID invalidId = UUID.randomUUID();
        when(productRepository.getProductById(invalidId)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> productService.deleteProductById(invalidId));
    }




}
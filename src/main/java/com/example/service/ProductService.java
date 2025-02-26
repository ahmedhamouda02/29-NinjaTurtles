package com.example.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.model.Product;
import com.example.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // ✅ Constructor Injection
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ Add Product with Validation
    public Product addProduct(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty.");
        }
        if (price < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be negative.");
        }
        Product product = new Product(name, price);
        productRepository.addProduct(product);
        return product;
    }

    // ✅ Get All Products
    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    // ✅ Get Product By ID with UUID Validation
    public Product getProductById(String productId) {
        try {
            UUID uuid = UUID.fromString(productId);
            Product product = productRepository.getProductById(uuid);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
            }
            return product;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format: " + productId);
        }
    }

    // ✅ Update Product with Validations
    public Product updateProduct(String productId, String newName, double newPrice) {
        try {
            UUID uuid = UUID.fromString(productId);
            if (newName == null || newName.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty.");
            }
            if (newPrice < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be negative.");
            }
            Product updatedProduct = productRepository.updateProduct(uuid, newName, newPrice);
            if (updatedProduct == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found for update.");
            }
            return updatedProduct;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format: " + productId);
        }
    }

    public void applyDiscount(double discount, ArrayList<String> productIds) {
        if (discount < 0 || discount > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Discount must be between 0% and 100%.");
        }
    
        ArrayList<UUID> validProductUUIDs = new ArrayList<>();
        ArrayList<String> invalidUUIDs = new ArrayList<>();
        ArrayList<String> notFoundUUIDs = new ArrayList<>();
    
        for (String productId : productIds) {
            try {
                UUID uuid = UUID.fromString(productId);
    
                // ✅ Check if the product exists before applying discount
                Product product = productRepository.getProductById(uuid);
                if (product == null) {
                    notFoundUUIDs.add(productId);
                } else {
                    validProductUUIDs.add(uuid);
                }
            } catch (IllegalArgumentException e) {
                // ✅ Collect invalid UUIDs separately
                invalidUUIDs.add(productId);
            }
        }
    
        // ✅ If any invalid UUIDs were found, return an error
        if (!invalidUUIDs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format: " + invalidUUIDs);
        }
    
        // ✅ If no valid products were found, return an error
        if (validProductUUIDs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No valid products found for discount. Invalid IDs: " + notFoundUUIDs);
        }
    
        // ✅ Apply discount only to valid products
        productRepository.applyDiscount(discount, validProductUUIDs);
    }
    
    // ✅ Delete Product with Validation
    public void deleteProductById(String productId) {
        try {
            UUID uuid = UUID.fromString(productId);
            Product product = productRepository.getProductById(uuid);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found for deletion.");
            }
            productRepository.deleteProductById(uuid);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format: " + productId);
        }
    }
}

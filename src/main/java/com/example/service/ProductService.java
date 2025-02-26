package com.example.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

   
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product addProduct(String name, double price) {
        Product product = new Product(name, price); 
        productRepository.addProduct(product);
        return product;
    }


    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

   
    public Product getProductById(UUID productId) {
        return productRepository.getProductById(productId);
    }


    public Product updateProduct(UUID productId, String newName, double newPrice) {
        return productRepository.updateProduct(productId, newName, newPrice);
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        productRepository.applyDiscount(discount, productIds);
    }


    public void deleteProductById(UUID productId) {
        productRepository.deleteProductById(productId);
    }
}

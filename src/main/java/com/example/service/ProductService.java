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

  @Autowired
  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product addProduct(Product product) {
    if (product.getName() == null || product.getName().trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty.");
    }
    if (product.getPrice() < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be negative.");
    }
    if (product.getId() == null) {
      product.setId(UUID.randomUUID());
    }
    productRepository.addProduct(product);
    return product;
  }

  public ArrayList<Product> getProducts() {
    return productRepository.getProducts();
  }

  public Product getProductById(UUID productId) {
    Product product = productRepository.getProductById(productId);
    if (product == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
    }
    return product;
  }

  public Product updateProduct(UUID productId, String newName, double newPrice) {
    if (newName == null || newName.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty.");
    }
    if (newPrice < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be negative.");
    }
    Product updatedProduct = productRepository.updateProduct(productId, newName, newPrice);
    if (updatedProduct == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found for update.");
    }
    return updatedProduct;
  }

  public void applyDiscount(double discount, ArrayList<UUID> productIds) {
    if (discount < 0 || discount > 100) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Discount must be between 0% and 100%.");
    }
    productRepository.applyDiscount(discount, productIds);
  }

  public void deleteProductById(UUID productId) {
    Product product = productRepository.getProductById(productId);
    if (product == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found for deletion.");
    }
    productRepository.deleteProductById(productId);
  }
}

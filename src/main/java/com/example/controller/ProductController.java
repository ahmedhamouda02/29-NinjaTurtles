package com.example.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.model.Product;
import com.example.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  // ✅ Add Product (Fixed to Accept `Product` Directly)
  @PostMapping("/")
  public Product addProduct(@RequestBody Product product) {
    return productService.addProduct(product);
  }

  // ✅ Get All Products
  @GetMapping("/")
  public ArrayList<Product> getProducts() {
    return productService.getProducts();
  }

  // ✅ Get a Product by ID (Fixed to Use UUID)
  @GetMapping("/{productId}")
  public Product getProductById(@PathVariable UUID productId) {
    return productService.getProductById(productId);
  }

  // ✅ Update a Product (Fixed to Use UUID)
  @PutMapping("/update/{productId}")
  public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String, Object> body) {
    String newName = (String) body.get("name");
    double newPrice = ((Number) body.get("price")).doubleValue();
    return productService.updateProduct(productId, newName, newPrice);
  }

  // ✅ Apply Discount (Fixed to Use UUID)
  @PutMapping("/applyDiscount")
  public String applyDiscount(@RequestParam double discount, @RequestBody ArrayList<UUID> productIds) {
    productService.applyDiscount(discount, productIds);
    return "Discount applied successfully.";
  }

  // ✅ Delete Product (Fixed to Use UUID)
  @DeleteMapping("/delete/{productId}")
  public String deleteProductById(@PathVariable UUID productId) {
    productService.deleteProductById(productId);
    return "Product deleted successfully.";
  }
}

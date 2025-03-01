package com.example.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ✅ Add a New Product with Error Handling
    @PostMapping("/")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> body) {
        try {
            // ✅ Validate if "name" exists
            if (!body.containsKey("name") || body.get("name") == null || body.get("name").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Product name cannot be empty.");
            }
            String name = body.get("name").toString();

            // ✅ Validate if "price" exists
            if (!body.containsKey("price") || body.get("price") == null) {
                return ResponseEntity.badRequest().body("Error: Product price is required.");
            }

            // ✅ Parse and validate price
            double price;
            try {
                price = Double.parseDouble(body.get("price").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Error: Product price must be a valid number.");
            }

            // ✅ Validate if price is negative
            if (price < 0) {
                return ResponseEntity.badRequest().body("Error: Product price cannot be negative.");
            }

            return ResponseEntity.ok(productService.addProduct(name, price));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // ✅ Get All Products
    @GetMapping("/")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    // ✅ Get a Product by ID with UUID Validation
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable UUID productId) {
        try {
            return ResponseEntity.ok(productService.getProductById(productId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Update a Product
    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody Map<String, Object> body) {
        try {
            // ✅ Validate if "name" exists
            if (!body.containsKey("name") || body.get("name") == null || body.get("name").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Product name cannot be empty.");
            }
            String newName = body.get("name").toString();

            // ✅ Validate if "price" exists
            if (!body.containsKey("price") || body.get("price") == null) {
                return ResponseEntity.badRequest().body("Error: Product price is required.");
            }

            // ✅ Parse and validate price
            double newPrice;
            try {
                newPrice = Double.parseDouble(body.get("price").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Error: Product price must be a valid number.");
            }

            // ✅ Validate if price is negative
            if (newPrice < 0) {
                return ResponseEntity.badRequest().body("Error: Product price cannot be negative.");
            }

            return ResponseEntity.ok(productService.updateProduct(productId, newName, newPrice));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred: " + e.getMessage());
        }
    }

    // ✅ Apply Discount with Error Handling
    @PutMapping("/applyDiscount")
    public ResponseEntity<?> applyDiscount(@RequestParam double discount, @RequestBody ArrayList<String> productIds) {
        try {
            // ✅ Validate discount percentage
            if (discount < 0 || discount > 100) {
                return ResponseEntity.badRequest().body("Error: Discount must be between 0% and 100%.");
            }

            productService.applyDiscount(discount, productIds);
            return ResponseEntity.ok("Discount applied successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Delete a Product with UUID Validation
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable String productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok("Product deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

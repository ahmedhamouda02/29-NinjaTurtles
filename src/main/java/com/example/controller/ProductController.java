package com.example.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @PostMapping("/")
    public Product addProduct(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        double price = (double) body.get("price");
        return productService.addProduct(name, price);
    }


    @GetMapping("/")
    public ArrayList<Product> getProducts() {
        return productService.getProducts();
    }


    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable UUID productId) {
        return productService.getProductById(productId);
    }


    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String, Object> body) {
        String newName = (String) body.get("name");
        double newPrice = (double) body.get("price");
        return productService.updateProduct(productId, newName, newPrice);
    }


    @PutMapping("/applyDiscount")
    public String applyDiscount(@RequestParam double discount, @RequestBody ArrayList<UUID> productIds) {
        productService.applyDiscount(discount, productIds);
        return "Discount applied successfully.";
    }

    
    @DeleteMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return "Product deleted successfully.";
    }
}

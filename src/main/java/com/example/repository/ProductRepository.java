package com.example.repository;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.model.Product;

@Repository
public class ProductRepository extends MainRepository<Product> {

    // ✅ Define the JSON file path
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/products.json";
    }

    // ✅ Define the array type for JSON conversion
    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    // ✅ Add a New Product and Save to JSON
    public void addProduct(Product product) {
        ArrayList<Product> products = findAll();  // Read current products
        products.add(product);  // Add new product
        saveAll(products);  // Save back to JSON
    }

    // ✅ Get All Products
    public ArrayList<Product> getProducts() {
        return findAll();  // Read from JSON
    }

    // ✅ Get Product By ID
    public Product getProductById(UUID productId) {
        return findAll().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    // ✅ Update Product and Save to JSON
    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = findAll();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                saveAll(products);  // Save updated list to JSON
                return product;
            }
        }
        return null;  // If product not found
    }

    // ✅ Apply Discount and Save to JSON
    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        ArrayList<Product> products = findAll();
        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double newPrice = product.getPrice() * ((100 - discount) / 100);
                product.setPrice(newPrice);
            }
        }
        saveAll(products);  // Save updated prices
    }

    // ✅ Delete Product and Save to JSON
    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();
        products.removeIf(product -> product.getId().equals(productId));
        saveAll(products); 
    }
}

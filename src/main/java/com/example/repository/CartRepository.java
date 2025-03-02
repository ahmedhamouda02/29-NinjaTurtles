package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json";
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }

    public Cart addCart(Cart cart){
        if(cart.getId() == null){
            cart.setId(UUID.randomUUID());
        }
        save(cart);
        return cart;
    }

    public ArrayList<Cart> getCarts(){
        return findAll();
    }

    public Cart getCartById(UUID cartId){
        return findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    public Cart getCartByUserId(UUID userId) {
        System.out.println("Retrieving carts...");
        List<Cart> carts = findAll();
        System.out.println("Retrieved carts: " + carts);

        return carts.stream()
                .filter(cart -> {
                    System.out.println("Checking cart: " + cart.getId() + " with userId: " + cart.getUserId());
                    return cart.getUserId().equals(userId);
                })
                .findFirst()
                .orElse(null);
    }



    public void addProductToCart(UUID cartId, Product product) {
        System.out.println("In cart repository, adding product to cart");
        ArrayList<Cart> carts = findAll();
        System.out.println("Retrieved carts: " + carts);

        Cart cart = getCartById(cartId);
        System.out.println("Got cart by ID: " + cartId + ": " + cart);
        if (cart == null) {
            System.out.println("Cart not found for ID: " + cartId);
            return; // Just exit instead of throwing an exception
        }

        // Ensure the cart has a products list
        if (cart.getProducts() == null) {
            cart.setProducts(new ArrayList<>());
        }

        // Remove the old cart entry
        carts.removeIf(c -> c.getId().equals(cartId));

        // Add the product
        cart.getProducts().add(product);
        System.out.println("Added product to cart " + cartId + ": " + product);

        // Re-add the updated cart
        carts.add(cart);

        // Override the data
        overrideData(carts);
        System.out.println("Updated cart list saved successfully.");
    }

    public void deleteProductFromCart(UUID cartId, Product product){
        Cart cart = getCartById(cartId);
        if(cart == null || cart.getProducts().isEmpty()){
            throw new RuntimeException("Cart not found");
        }
        cart.getProducts().remove(product);
        saveAll(getCarts());
    }

    public void deleteCartById(UUID cartId){
        ArrayList<Cart> carts = findAll();
        for(Cart c : carts){
            if(c.getId().equals(cartId)){
                carts.remove(c);
                break;
            }
        }
        overrideData(carts);
    }


}

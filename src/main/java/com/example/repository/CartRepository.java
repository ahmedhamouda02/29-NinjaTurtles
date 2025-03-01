package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

<<<<<<< HEAD
    public Cart addCart(Cart cart) {
        if (cart.getId() == null) {
=======
    public Cart addCart(Cart cart){
        if(cart.getId() == null){
>>>>>>> main
            cart.setId(UUID.randomUUID());
        }
        save(cart);
        return cart;
    }

<<<<<<< HEAD
    public ArrayList<Cart> getCarts() {
        return findAll();
    }

    public Cart getCartById(UUID cartId) {
=======
    public ArrayList<Cart> getCarts(){
        return findAll();
    }

    public Cart getCartById(UUID cartId){
>>>>>>> main
        return findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

<<<<<<< HEAD
    public Cart getCartByUserId(UUID userId) {
=======
    public Cart getCartByUserId(UUID userId){
>>>>>>> main
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }


<<<<<<< HEAD
    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        Cart cart = getCartById(cartId);
        if (cart == null) {
=======
    public void addProductToCart(UUID cartId, Product product){
        ArrayList<Cart> carts = findAll();
        Cart cart = getCartById(cartId);
        if(cart == null){
>>>>>>> main
            throw new RuntimeException("Cart not found");
        }
        carts.removeIf(c -> c.getId().equals(cartId));
        cart.getProducts().add(product);
        carts.add(cart);
        overrideData(carts);


<<<<<<< HEAD
    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        Cart cart = getCartById(cartId);
        if (cart == null) {
=======

    }
    public void deleteProductFromCart(UUID cartId, Product product){
        Cart cart = getCartById(cartId);
        if(cart == null){
>>>>>>> main
            throw new RuntimeException("Cart not found");
        }
        cart.getProducts().remove(product);
        saveAll(getCarts());
    }

<<<<<<< HEAD
    public void deleteCartById(UUID cartId) {
=======
    public void deleteCartById(UUID cartId){
>>>>>>> main
        ArrayList<Cart> carts = findAll();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        overrideData(carts);
    }

}

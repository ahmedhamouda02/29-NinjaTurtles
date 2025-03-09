
package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {
    CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart addCart(Cart cart){
        if(cart.getUserId() == null){
            throw new IllegalArgumentException("User ID is required for creating a cart.");
        }
//        if (cart.getProducts().isEmpty()){
//            throw new IllegalArgumentException("Cart must have at least one product.");
//        }
        for(Product p : cart.getProducts()){
            if(p.getId() == null || p.getName() == null || p.getPrice() < 0){
                throw new IllegalArgumentException("Each product must have a valid ID, name, and price.");
            }
        }
        cart.setId(UUID.randomUUID());
        return cartRepository.addCart(cart);
    }

    public ArrayList<Cart> getCarts(){
        return cartRepository.getCarts();
    }

    public Cart getCartById(UUID cartId){
        Cart cart = cartRepository.getCartById(cartId);
        if(cart == null){
            throw new IllegalArgumentException("Cart not found");
        }
        return cart;
    }
    public Cart getCartByUserId(UUID userId){
        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found");
        }
        return cart;

    }
    public void addProductToCart(UUID userId, Product product) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setId(UUID.randomUUID());
            newCart.setUserId(userId);
            newCart.setProducts(new ArrayList<>(List.of(product)));
            cartRepository.addCart(newCart);
        } else {
            cart.getProducts().add(product);
            cartRepository.updateCart(cart); 
        }
    }

    public void deleteProductFromCart(UUID cartId, Product product){
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }
        cartRepository.deleteProductFromCart(cartId, product);
    }
    public void deleteCartById(UUID cartId){
        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }
        cartRepository.deleteCartById(cartId);
    }


}
package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {
    CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart addCart(Cart cart) {
        if (cart.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required for creating a cart.");
        }
        for (Product p : cart.getProducts()) {
            if (p.getId() == null || p.getName() == null || p.getPrice() < 0) {
                throw new IllegalArgumentException("Each product must have a valid ID, name, and price.");
            }
        }
        cart.setId(UUID.randomUUID());
        return cartRepository.addCart(cart);
    }

    public ArrayList<Cart> getCarts() {
        return cartRepository.getCarts();
    }

    public Cart getCartById(UUID cartId) {
        Cart cart = cartRepository.getCartById(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found");
        }
        return cart;
    }

    public Cart getCartByUserId(UUID userId) {
        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found");
        }
        return cart;
    }

    public void addProductToCart(UUID cartId, Product product) {
        if (cartRepository.getCartById(cartId) == null) {
            throw new IllegalArgumentException("Cart not found");
        }
        cartRepository.addProductToCart(cartId, product);
    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        cartRepository.deleteProductFromCart(cartId, product);
    }

    public void deleteCartById(UUID cartId) {
        cartRepository.deleteCartById(cartId);
    }


}

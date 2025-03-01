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

    public Cart addCart(Cart cart) {
        if (cart.getId() == null) {
            cart.setId(UUID.randomUUID());
        }
        save(cart);
        return cart;
    }

    public ArrayList<Cart> getCarts() {
        return findAll();
    }

    public Cart getCartById(UUID cartId) {
        return findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    public Cart getCartByUserId(UUID userId) {
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }


    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        Cart cart = getCartById(cartId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        carts.removeIf(c -> c.getId().equals(cartId));
        cart.getProducts().add(product);
        carts.add(cart);
        overrideData(carts);


    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        Cart cart = getCartById(cartId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        cart.getProducts().remove(product);
        saveAll(getCarts());
    }

    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = findAll();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        overrideData(carts);
    }

}

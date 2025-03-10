package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

  @Value("${spring.application.cartDataPath}")
  private String cartDataPath;
  @Override
  protected String getDataPath() {
    return cartDataPath;
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
    System.out.println("In cart repository, getting cart by user ID");
    ArrayList<Cart> carts = findAll();
    System.out.println("Retrieved carts: " + carts);
    for (Cart c : carts) {
      if (c.getUserId().equals(userId)) {
        System.out.println("Found cart by user ID: " + userId + ": " + c);
        return c;
      }
    }
    System.out.println("Cart not found for user ID: " + userId);
    return null;
  }

  public void updateCart(Cart updatedCart) {
    ArrayList<Cart> carts = findAll();
    for (int i = 0; i < carts.size(); i++) {
      if (carts.get(i).getId().equals(updatedCart.getId())) {
        carts.set(i, updatedCart);
        break;
      }
    }
    overrideData(carts);
  }

  public void addProductToCart(UUID cartId, Product product) {
    System.out.println("In cart repository, adding product to cart");
    ArrayList<Cart> carts = findAll();
    System.out.println("Retrieved carts: " + carts);

    Cart cart = getCartById(cartId);
    System.out.println("Got cart by ID: " + cartId + ": " + cart);
    if (cart == null) {
      System.out.println("Cart not found for ID: " + cartId);
      return;
    }

    if (cart.getProducts() == null) {
      cart.setProducts(new ArrayList<>());
    }

    carts.removeIf(c -> c.getId().equals(cartId));

    cart.getProducts().add(product);
    System.out.println("Added product to cart " + cartId + ": " + product);

    carts.add(cart);

    overrideData(carts);
    System.out.println("Updated cart list saved successfully.");
  }

  public void deleteProductFromCart(UUID cartId, Product product) {
    ArrayList<Cart> carts = findAll();
    boolean cartFound = false;


    for (Cart cart : carts) {
      if (cart.getId().equals(cartId)) {
        cartFound = true;
        cart.getProducts().removeIf(p -> p.getId().equals(product.getId()));
        break;
      }
    }

    if (!cartFound) {
      throw new RuntimeException("Cart not found");
    }

    overrideData(carts);
    System.out.println("Product removed from cart");
  }

  public void deleteCartById(UUID cartId) {
    ArrayList<Cart> carts = findAll();
    for (Cart c : carts) {
      if (c.getId().equals(cartId)) {
        carts.remove(c);
        break;
      }
    }
    overrideData(carts);
  }

}

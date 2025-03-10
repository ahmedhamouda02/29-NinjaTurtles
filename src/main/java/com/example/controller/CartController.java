package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/")
    public Cart addCart(@RequestBody Cart cart){
        try {
            return cartService.addCart(cart);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException( "Error Adding Cart");
        }
    }

    @GetMapping("/")
    public ArrayList<Cart> getCarts(){
        return cartService.getCarts();
    }

    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable UUID cartId){
        return cartService.getCartById(cartId);
    }

    @PutMapping("/addProduct/{cartId}")
    public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product){
        try {
            cartService.addProductToCart(cartId, product);
            return "Product added to cart";
        } catch (IllegalArgumentException e){
            return "Either cart not found or product is invalid";
        }
    }

    @DeleteMapping("/delete/{cartId}")
    public String deleteCartById(@PathVariable UUID cartId){
        try {
            cartService.deleteCartById(cartId);
            return "Cart deleted successfully";
        } catch (IllegalArgumentException e){
            return "Error deleting cart";
        }
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable UUID userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            if (cart == null) {
                return ResponseEntity.status(HttpStatus.OK).body("Cart not found for this user");
            }
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


}
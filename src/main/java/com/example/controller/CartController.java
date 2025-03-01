package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
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
<<<<<<< HEAD
    public Cart addCart(@RequestBody Cart cart) {
        try {
            return cartService.addCart(cart);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
=======
    public Cart addCart(@RequestBody Cart cart){
        try {
            return cartService.addCart(cart);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException( "Error Adding Cart");
>>>>>>> main
        }
    }

    @GetMapping("/")
<<<<<<< HEAD
    public ArrayList<Cart> getCarts() {
=======
    public ArrayList<Cart> getCarts(){
>>>>>>> main
        return cartService.getCarts();
    }

    @GetMapping("/{cartId}")
<<<<<<< HEAD
    public Cart getCartById(@PathVariable UUID cartId) {
=======
    public Cart getCartById(@PathVariable UUID cartId){
>>>>>>> main
        return cartService.getCartById(cartId);
    }

    @PutMapping("/addProduct/{cartId}")
<<<<<<< HEAD
    public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product) {
        try {
            cartService.addProductToCart(cartId, product);
            return "Product added to cart";
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
=======
    public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product){
        try {
            cartService.addProductToCart(cartId, product);
            return "Product added to cart";
        } catch (IllegalArgumentException e){
            return "Either cart not found or product is invalid";
>>>>>>> main
        }
    }

    @DeleteMapping("/delete/{cartId}")
<<<<<<< HEAD
    public String deleteCartById(@PathVariable UUID cartId) {
        try {
            cartService.deleteCartById(cartId);
            return "Cart deleted successfully";
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
=======
    public String deleteCartById(@PathVariable UUID cartId){
        try {
            cartService.deleteCartById(cartId);
            return "Cart deleted successfully";
        } catch (IllegalArgumentException e){
            return "Error deleting cart";
>>>>>>> main
        }
    }


}

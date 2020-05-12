package com.infy.EcommerceUserManagement.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.EcommerceUserManagement.model.Cart;
import com.infy.EcommerceUserManagement.service.CartService;

@RestController
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@PostMapping(value = "api/cart/add")
	public void addToCart(@RequestBody Cart cart) {
		cartService.addToCart(cart);
	}
	
	@DeleteMapping(value = "api/cart/remove")
	public void removeFromCart(@RequestBody Cart cart) {
		cartService.removeFromCart(cart);
		
	}
	
	@GetMapping(value = "api/cart")
	public List<Cart> getAllCartItem(@RequestParam(name = "buyerId") int buyerId) {
		return cartService.getAllCartItem(buyerId);
		
	}
	
	@GetMapping(value="api/cart/checkout")
	public List<Cart> checkOutFromCart(@RequestParam(name = "buyerId") int buyerId){
		return cartService.checkOutFromCart(buyerId);
		
	}
	


	

}

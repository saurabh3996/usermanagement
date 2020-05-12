package com.infy.EcommerceUserManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.EcommerceUserManagement.model.Wishlist;
import com.infy.EcommerceUserManagement.service.WishListService;

@RestController
public class WishListController {
	
	@Autowired
	WishListService wishListService;
	
	@PostMapping(value = "api/wishlist/add")
	public void addToWishList(@RequestBody Wishlist wishlist) {
		wishListService.addToWishList(wishlist);
	
	}
	
	@DeleteMapping(value = "api/wishlist/remove")
	public void removeFromWishList(@RequestBody Wishlist wishlist) {
		wishListService.removeFromWishList(wishlist);
	}
	
	@GetMapping(value = "api/wishlist")
	public List<Wishlist> getAllWishList(@RequestParam(name ="buyerId") int buyerId)
	{
		
		return wishListService.getAllWishList(buyerId);
	}
	
	@PutMapping(value = "api/wishlist/move")
	public void addToCartFromWishlist(@RequestBody Wishlist wishlist,@RequestParam(name = "quantity") int quantity) {
		
		wishListService.wishListMoveToCart(wishlist,quantity);
		
	}
}

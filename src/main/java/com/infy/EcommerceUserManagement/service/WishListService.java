package com.infy.EcommerceUserManagement.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.EcommerceUserManagement.controller.CartController;
import com.infy.EcommerceUserManagement.dao.WishListRepository;
import com.infy.EcommerceUserManagement.entity.WishListId;
import com.infy.EcommerceUserManagement.entity.WishlistEntity;
import com.infy.EcommerceUserManagement.model.Cart;
import com.infy.EcommerceUserManagement.model.Wishlist;



@Service
public class WishListService {

	@Autowired
	WishListRepository wishListRepository;
	
	@Autowired
	CartController cartController;

	public void addToWishList(Wishlist wishlist) {
		WishlistEntity wishlistEntity = new WishlistEntity(new WishListId(wishlist.getBuyerId(), wishlist.getProdId()));
		wishListRepository.save(wishlistEntity);

	}

	public void removeFromWishList(Wishlist wishlist) {
		wishListRepository.deleteById(new WishListId(wishlist.getBuyerId(), wishlist.getProdId()));
	}

	public List<Wishlist> getAllWishList(int buyerId) {
		List<WishlistEntity> allWishlistEntity = wishListRepository.findByIdBuyerId(buyerId);

		List<Wishlist> allWIshlistmodal = new ArrayList<Wishlist>();
		for (WishlistEntity wishlistEntity : allWishlistEntity) {
			Wishlist wishlist = new Wishlist(wishlistEntity.getId().getBuyerId(), wishlistEntity.getId().getProdId());
			allWIshlistmodal.add(wishlist);
		}
		return allWIshlistmodal;

	}
	
	public void wishListMoveToCart(Wishlist wishlist,int quantity) {
		Cart cart = new Cart(wishlist.getBuyerId(),wishlist.getProdId(),quantity);
		cartController.addToCart(cart);
		removeFromWishList(wishlist);
		
			
		
	}
}

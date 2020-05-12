package com.infy.EcommerceUserManagement.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.EcommerceUserManagement.dao.CartRepository;
import com.infy.EcommerceUserManagement.entity.CartEntity;
import com.infy.EcommerceUserManagement.entity.CartId;
import com.infy.EcommerceUserManagement.model.Cart;

@Service
public class CartService {
	
	@Autowired
	CartRepository cartRepository;
	
	public void addToCart(Cart cart) {
		
		CartEntity cartEntity=new CartEntity(new CartId(cart.getBuyerId(),cart.getProdId()),cart.getQuantity());
		cartRepository.save(cartEntity);
	}
	
	public void removeFromCart(Cart cart) {
		cartRepository.deleteById(new CartId(cart.getBuyerId(),cart.getProdId()));
	}
	
	public List<Cart> getAllCartItem(int buyerId){
		Iterable<CartEntity> cartEntity = cartRepository.findAll();
		List<Cart> cartList= new ArrayList<Cart>();
		for (CartEntity cartEntity2 : cartEntity) {
			if (cartEntity2.getCartId().getBuyerId()==buyerId) {
				Cart cart=new Cart(cartEntity2.getCartId().getBuyerId(),cartEntity2.getCartId().getProdId(),cartEntity2.getQuantity());
				cartList.add(cart);
			}
			
		}
		
		return cartList;
	}
	
	public List<Cart> checkOutFromCart(int buyerId){
		List<Cart> cartList= new ArrayList<Cart>();
		Iterable<CartEntity> cartEntity = cartRepository.findAll();
		for (CartEntity cartEntity2 : cartEntity) {
			if(cartEntity2.getCartId().getBuyerId()==buyerId) {
				Cart cart=new Cart(cartEntity2.getCartId().getBuyerId(),cartEntity2.getCartId().getProdId(),cartEntity2.getQuantity());
				cartList.add(cart);
				removeFromCart(cart);
			}
			
			
		}
		
		return cartList;
		
		
	
				
	}
	

}

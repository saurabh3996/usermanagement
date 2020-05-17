package com.infy.EcommerceUserManagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.infy.EcommerceUserManagement.controller.BuyerController;
import com.infy.EcommerceUserManagement.dao.CartRepository;
import com.infy.EcommerceUserManagement.entity.CartEntity;
import com.infy.EcommerceUserManagement.entity.CartId;
import com.infy.EcommerceUserManagement.exception.CartItemNotExistException;
import com.infy.EcommerceUserManagement.exception.NotPrivilegeBuyerException;
import com.infy.EcommerceUserManagement.exception.StockNotAvailableException;
import com.infy.EcommerceUserManagement.exception.UserException;
import com.infy.EcommerceUserManagement.model.Cart;
import com.infy.EcommerceUserManagement.model.Product;

@Service
public class CartService {

	@Autowired
	CartRepository cartRepository;

	@Value("${productAPIURL}")
	public String productAPIURI;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	BuyerController buyerController;

	public void addToCart(Cart cart) throws UserException {

		final String baseUrl = productAPIURI + cart.getProdId();
		ResponseEntity<Product> result = restTemplate.getForEntity(baseUrl, Product.class);
		int stock = result.getBody().getStock();

		Optional<CartEntity> cartEntity = cartRepository.findById(new CartId(cart.getBuyerId(), cart.getProdId()));
		if (cartEntity.isPresent()) {
			if (buyerController.isBuyerPrivileged(cart.getBuyerId())) {
				if (cart.getQuantity() + cartEntity.get().getQuantity() <= stock) {
					cart.setQuantity(cart.getQuantity() + cartEntity.get().getQuantity());
					CartEntity cartEntity1 = new CartEntity(new CartId(cart.getBuyerId(), cart.getProdId()),
							cart.getQuantity());
					cartRepository.save(cartEntity1);

				} else {
					throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");
				}

			} else {
				if (cart.getQuantity() + cartEntity.get().getQuantity() <= stock
						&& cart.getQuantity() + cartEntity.get().getQuantity() <= 10) {
					cart.setQuantity(cart.getQuantity() + cartEntity.get().getQuantity());
					CartEntity cartEntity1 = new CartEntity(new CartId(cart.getBuyerId(), cart.getProdId()),
							cart.getQuantity());

					cartRepository.save(cartEntity1);
				}

				else {
					if (cart.getQuantity() + cartEntity.get().getQuantity() > 10) {
						throw new NotPrivilegeBuyerException("wishlist.NOT_PRIVILEGE_BUYER");

					} else {
						throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");

					}
				}

			}

		} 
		
		else {
			if (buyerController.isBuyerPrivileged(cart.getBuyerId())) {
				if (cart.getQuantity() <= stock) {
					CartEntity cartEntity1 = new CartEntity(new CartId(cart.getBuyerId(), cart.getProdId()),
							cart.getQuantity());
					cartRepository.save(cartEntity1);

				} else {
					throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");
				}

			} else {

				if (cart.getQuantity() <= 10 && cart.getQuantity() <= stock) {
					CartEntity cartEntity1 = new CartEntity(new CartId(cart.getBuyerId(), cart.getProdId()),
							cart.getQuantity());
					cartRepository.save(cartEntity1);

				} else {
					if (cart.getQuantity() > stock) {
						throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");
					} else {
						throw new NotPrivilegeBuyerException("wishlist.NOT_PRIVILEGE_BUYER");

					}
				}
			}

		}

	}

	public void removeFromCart(Cart cart) throws UserException {
		
		Optional<CartEntity> cartEntity = cartRepository.findById(new CartId(cart.getBuyerId(), cart.getProdId()));
		if(cartEntity.isPresent()) {
			cartRepository.deleteById(new CartId(cart.getBuyerId(), cart.getProdId()));
			}
		else {
			throw new CartItemNotExistException("cart.ITEM_NOT_IN_CART");
		}
	}
	

	public List<Cart> getAllCartItem(int buyerId) {
		Iterable<CartEntity> cartEntity = cartRepository.findAll();
		List<Cart> cartList = new ArrayList<Cart>();
		for (CartEntity cartEntity2 : cartEntity) {
			if (cartEntity2.getCartId().getBuyerId() == buyerId) {
				Cart cart = new Cart(cartEntity2.getCartId().getBuyerId(), cartEntity2.getCartId().getProdId(),
						cartEntity2.getQuantity());
				cartList.add(cart);
			}

		}

		return cartList;
	}

	public List<Cart> checkOutFromCart(int buyerId) {
		List<Cart> cartList = new ArrayList<Cart>();
		Iterable<CartEntity> cartEntity = cartRepository.findAll();
		for (CartEntity cartEntity2 : cartEntity) {
			if (cartEntity2.getCartId().getBuyerId() == buyerId) {
				Cart cart = new Cart(cartEntity2.getCartId().getBuyerId(), cartEntity2.getCartId().getProdId(),
						cartEntity2.getQuantity());
				cartList.add(cart);
				try {
					removeFromCart(cart);
				} catch (UserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return cartList;

	}

}

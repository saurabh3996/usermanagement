package com.infy.EcommerceUserManagement.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.infy.EcommerceUserManagement.controller.BuyerController;
import com.infy.EcommerceUserManagement.controller.CartController;
import com.infy.EcommerceUserManagement.dao.CartRepository;
import com.infy.EcommerceUserManagement.dao.WishListRepository;
import com.infy.EcommerceUserManagement.entity.WishListId;
import com.infy.EcommerceUserManagement.entity.WishlistEntity;
import com.infy.EcommerceUserManagement.exception.NotPrivilegeBuyerException;
import com.infy.EcommerceUserManagement.exception.StockNotAvailableException;
import com.infy.EcommerceUserManagement.exception.UserException;
import com.infy.EcommerceUserManagement.exception.WishListEmptyException;
import com.infy.EcommerceUserManagement.exception.WishlistAlreadyExist;
import com.infy.EcommerceUserManagement.exception.WishlistNotAvailableException;
import com.infy.EcommerceUserManagement.model.Cart;
import com.infy.EcommerceUserManagement.model.Product;
import com.infy.EcommerceUserManagement.model.Wishlist;

@Service
public class WishListService {

	@Value("${productAPIURL}")
	public String productAPIURI;

	@Autowired
	WishListRepository wishListRepository;

	@Autowired
	CartController cartController;

	@Autowired
	BuyerController buyerController;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CartRepository cartRepository;

	public void addToWishList(Wishlist wishlist) throws UserException {
		WishlistEntity wishlistEntity = wishListRepository.findByIdBuyerIdAndIdProdId(wishlist.getBuyerId(),
				wishlist.getProdId());
		if (wishlistEntity == null) {
			WishlistEntity newwishlistEntity = new WishlistEntity(
					new WishListId(wishlist.getBuyerId(), wishlist.getProdId()));
			wishListRepository.save(newwishlistEntity);
		} else {
			throw new WishlistAlreadyExist("wishlist.ALREADY_EXISTS");

		}

	}

	public void removeFromWishList(Wishlist wishlist) throws UserException {
		WishlistEntity wishlistEntity = wishListRepository.findByIdBuyerIdAndIdProdId(wishlist.getBuyerId(),
				wishlist.getProdId());
		if (wishlistEntity != null) {
			wishListRepository.deleteById(new WishListId(wishlist.getBuyerId(), wishlist.getProdId()));
		} else {
			throw new WishlistNotAvailableException("wishlist.NOT_AVAILABLE");
		}
	}

	public List<Wishlist> getAllWishList(int buyerId) throws UserException {
		List<WishlistEntity> allWishlistEntity = wishListRepository.findByIdBuyerId(buyerId);
		if (allWishlistEntity != null) {
			List<Wishlist> allWIshlistmodal = new ArrayList<Wishlist>();
			for (WishlistEntity wishlistEntity : allWishlistEntity) {
				Wishlist wishlist = new Wishlist(wishlistEntity.getId().getBuyerId(),
						wishlistEntity.getId().getProdId());
				allWIshlistmodal.add(wishlist);
			}
			return allWIshlistmodal;
		} else {
			throw new WishListEmptyException("wishlist.EMPTY");
		}

	}

	public void wishListMoveToCart(Wishlist wishlist, int quantity) throws UserException {
		// check the privilege
		WishlistEntity wishlistEntity = wishListRepository.findByIdBuyerIdAndIdProdId(wishlist.getBuyerId(),
				wishlist.getProdId());
		if (wishlistEntity != null) {
			if (buyerController.isBuyerPrivileged(wishlist.getBuyerId())) {

				final String baseUrl = productAPIURI + wishlist.getProdId();
				ResponseEntity<Product> result = restTemplate.getForEntity(baseUrl, Product.class);
				if (result.getBody().getStock() >= quantity) {
					Cart cart = new Cart(wishlist.getBuyerId(), wishlist.getProdId(), quantity);

					List<Cart> cartlist = cartController.getAllCartItem(wishlist.getBuyerId());

					if (cartlist.size() == 0) {
						removeFromWishList(wishlist);
						cartController.addToCart(cart);
					} else {
						for (Cart cart2 : cartlist) {
							if (cart2.getProdId() == wishlist.getProdId()) {
								if (cart2.getQuantity() + quantity <= result.getBody().getStock()) {
									removeFromWishList(wishlist);
									cart2.setQuantity(cart2.getQuantity() + quantity);
									cartController.addToCart(cart2);
									

								} else {
									throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");

								}
							}

						}
					}

				} else {
					throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");
				}

			} else {
				if (quantity < 10) {
					final String baseUrl = productAPIURI + wishlist.getProdId();
					ResponseEntity<Product> result = restTemplate.getForEntity(baseUrl, Product.class);
					if (result.getBody().getStock() >= quantity) {

						Cart cart = new Cart(wishlist.getBuyerId(), wishlist.getProdId(), quantity);

						List<Cart> cartlist = cartController.getAllCartItem(wishlist.getBuyerId());

						if (cartlist.size() == 0) {
							removeFromWishList(wishlist);
							cartController.addToCart(cart);
						} else {
							for (Cart cart2 : cartlist) {
								if (cart2.getProdId() == wishlist.getProdId()) {
									if (cart2.getQuantity() + quantity <= result.getBody().getStock() && cart2.getQuantity() + quantity<=10 ) {
										removeFromWishList(wishlist);
										cart2.setQuantity(cart2.getQuantity() + quantity);
										cartController.addToCart(cart2);
										

									} else {
										throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");

									}
								}

							}
						}

					} else {
						throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");
					}

				} else {
					throw new NotPrivilegeBuyerException("wishlist.NOT_PRIVILEGE_BUYER");
				}

			}
		} else {
			throw new WishlistNotAvailableException("wishlist.NOT_AVAILABLE");
		}

	}
}

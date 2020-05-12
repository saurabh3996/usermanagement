package com.infy.EcommerceUserManagement.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.EcommerceUserManagement.entity.WishListId;
import com.infy.EcommerceUserManagement.entity.WishlistEntity;

public interface WishListRepository extends CrudRepository<WishlistEntity, WishListId>{
	public List<WishlistEntity> findByIdBuyerId(int buyerId);
	
	public WishlistEntity findByIdBuyerIdAndIdProdId(int buyerId,int ProdId);
}

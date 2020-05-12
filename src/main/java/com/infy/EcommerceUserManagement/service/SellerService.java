package com.infy.EcommerceUserManagement.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.infy.EcommerceUserManagement.dao.SellerRepository;
import com.infy.EcommerceUserManagement.entity.SellerEntity;
import com.infy.EcommerceUserManagement.model.Seller;



@Service
public class SellerService {
	
	@Autowired
	SellerRepository sellerRepository;

	public void sellerRegisterion(Seller seller) {
		SellerEntity sellerEntity = new SellerEntity();
		BeanUtils.copyProperties(seller, sellerEntity);
		sellerRepository.save(sellerEntity);
	}
	
	public String sellerLogin(Seller  seller) {
		SellerEntity sellerEntity=sellerRepository.findBySellerId(seller.getSellerId());
		if(sellerEntity!= null) {
			if(sellerEntity.getPassword().equals(seller.getPassword())) {
				return "success";
			}
			else {
				return "Wrong password";
			}
			
		}
		else {
			
			return "Wrong Seller ID";
		}
	}
	
	public String deactivateSeller(Seller seller) {
		SellerEntity sellerEntity=sellerRepository.findBySellerId(seller.getSellerId());
		if(sellerEntity!= null) {
			sellerEntity.setActive(false);
			sellerRepository.save(sellerEntity);
			return "Deactivated successfully";
		}
		else {
			return "Invalid SellerId";
		}
				
	}
	
	public Seller getSellerDetails(int sellerId) {
		SellerEntity sellerEntity=sellerRepository.findBySellerId(sellerId);
		Seller seller = new Seller();
		if(sellerEntity!= null) {
		
		BeanUtils.copyProperties(sellerEntity, seller);
		return seller;
		}
		else {
			return seller;
			
		}
	}


}

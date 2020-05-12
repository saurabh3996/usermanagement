package com.infy.EcommerceUserManagement.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.EcommerceUserManagement.dao.BuyerRepository;
import com.infy.EcommerceUserManagement.entity.BuyerEntity;
import com.infy.EcommerceUserManagement.model.Buyer;

@Service
public class BuyerService {

	@Autowired
	BuyerRepository buyerRepository;

	public void buyerRegisterion(Buyer buyer) {
		BuyerEntity buyerEntity = new BuyerEntity();
		BeanUtils.copyProperties(buyer, buyerEntity);
		buyerRepository.save(buyerEntity);
	}

	public String buyerLogin(Buyer buyer) {

		BuyerEntity buyerEntity = buyerRepository.findByBuyerId(buyer.getBuyerId());
		if (buyerEntity != null) {
			if (buyerEntity.getPassword().equals(buyer.getPassword())) {
				return "Success";
			} else {
				return "Invalid Password";
			}

		} else {
			return "Wrong BuyerId";
		}

	}

	public String deactivateBuyer(Buyer buyer) {

		BuyerEntity buyerEntity = buyerRepository.findByBuyerId(buyer.getBuyerId());
		if (buyerEntity != null) {
			buyerEntity.setActive(false);
			buyerRepository.save(buyerEntity);
			return "Deactivated succesfully";

		} else {
			return "Invalid BuyerId";
		}

	}
	
	public int getRewardPoint(int buyerId) {
		BuyerEntity buyerEntity=buyerRepository.findByBuyerId(buyerId);
		return buyerEntity.getRewardPoints();

		
	}
	
	public void updateRewardPoint(int buyerId, int point) {
		BuyerEntity buyerEntity =buyerRepository.findByBuyerId(buyerId);
		buyerEntity.setRewardPoints(point+buyerEntity.getRewardPoints());
		buyerRepository.save(buyerEntity);
		
	}
	
	public boolean IsPrivileged(int buyerId) {
		
		BuyerEntity buyerEntity= buyerRepository.findByBuyerId(buyerId);
		
		if((buyerEntity.isPrivileged())==false) {
			
			return false;
		}
		else {
			
			return true;
		}
		
	}
	
	public void updateBuyerPrivilege(int buyerId,boolean privilege) {
		BuyerEntity buyerEntity= buyerRepository.findByBuyerId(buyerId);
		buyerEntity.setPrivileged(privilege);
	    buyerRepository.save(buyerEntity);
		
	}
	
	public Buyer getBuyerDetails(int buyerId) {
		BuyerEntity buyerEntity= buyerRepository.findByBuyerId(buyerId);
		Buyer buyer=new Buyer();
		BeanUtils.copyProperties(buyerEntity, buyer);
		return buyer;
		
	}
	
	
	
	
	
	
	
}

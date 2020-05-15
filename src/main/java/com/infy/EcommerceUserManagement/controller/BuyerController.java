package com.infy.EcommerceUserManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.infy.EcommerceUserManagement.model.Buyer;
import com.infy.EcommerceUserManagement.service.BuyerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;


@RestController
public class BuyerController {

	@Autowired
	BuyerService buyerservice;
    
	private static final Logger LOGGER = LoggerFactory.getLogger(BuyerController.class);
	
	@PostMapping(value = "api/buyer/register")
	public String registerBuyer(@RequestBody Buyer buyer) {
		
		buyerservice.buyerRegisterion(buyer);
		return "You Have Registered successfully";

	}
	
	  @PostMapping(value = "api/buyer/login") 
	  public String buyerLogin(@RequestBody Buyer buyer) {
	  
	  return buyerservice.buyerLogin(buyer);
	  
	  }
	  
	  
	  @PostMapping(value = "api/buyer/deactivate")
	  public String deactivateBuyer(@RequestBody Buyer buyer) {
		  
		  return buyerservice.deactivateBuyer(buyer);
		    }
	  
	  @GetMapping(value = "api/rewardPoint")
	  public int getRewardPoints(@RequestParam(name = "buyerId") int buyerId) {
		
		  LOGGER.info("I am inside reward point api");
		  return buyerservice.getRewardPoint(buyerId);
		  
		  
		  
	  }
	  
	  @PutMapping(value = "api/rewardPoint/update")
	  public void updateRewardPoint(@RequestParam(name = "buyerId") int buyerId ,@RequestParam(name = "point") int point) {
		  buyerservice.updateRewardPoint(buyerId, point);
		  
	  }
	  
	 @GetMapping(value = "api/buyer/isPrivilege")
	 public boolean isBuyerPrivileged(@RequestParam(name = "buyerId") int buyerId) {
		
		 return buyerservice.IsPrivileged(buyerId);
	 }
	 
	 @PutMapping(value = "api/buyer/privilege")
	 public void updateBuyerPrivilege(@RequestParam(name = "buyerId") int buyerId ,@RequestParam(name = "privilege") boolean privilege) {
		 buyerservice.updateBuyerPrivilege(buyerId, privilege);
		 
	 }
	 
	 @GetMapping(value = "api/buyer")
	 public Buyer getBuyerDetails(@RequestParam(name = "buyerId") int buyerId)
	 {
		 return buyerservice.getBuyerDetails(buyerId);
		 
	 }
	 
}

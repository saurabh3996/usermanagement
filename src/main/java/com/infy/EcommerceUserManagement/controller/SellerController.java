package com.infy.EcommerceUserManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.EcommerceUserManagement.model.Seller;
import com.infy.EcommerceUserManagement.service.SellerService;

@RestController
public class SellerController {
	
	@Autowired
	private SellerService sellerService;
	

	@PostMapping(value = "/api/seller/register")
	public String registerBuyer(@RequestBody Seller seller) {
		
		sellerService.sellerRegisterion(seller);
		return "You Have Registered successfully";

	}
	
	@PostMapping(value = "/api/seller/login")
	public String sellerLogin(@RequestBody Seller seller) {
		
		return sellerService.sellerLogin(seller);
	}
	
	@PostMapping(value = "api/seller/deactivate")
	public String deactivateSeller(@RequestBody Seller seller) {
	  return sellerService.deactivateSeller(seller);
	}
	
	@GetMapping(value = "api/seller")
	public Seller getSellerDetails(@RequestParam(name = "sellerId") int sellerId) {
		return sellerService.getSellerDetails(sellerId) ;
	}

}

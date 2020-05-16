package com.infy.EcommerceUserManagement.dao;

import org.springframework.data.repository.CrudRepository;

import com.infy.EcommerceUserManagement.entity.BuyerEntity;

public interface BuyerRepository extends CrudRepository<BuyerEntity, Integer> {

	public BuyerEntity findByBuyerId(int Id);
	public BuyerEntity findByEmail(String emailId);
	public BuyerEntity findByPhoneNumber(String phoneNumber);
}

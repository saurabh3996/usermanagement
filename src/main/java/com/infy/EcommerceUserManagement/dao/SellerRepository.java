package com.infy.EcommerceUserManagement.dao;

import org.springframework.data.repository.CrudRepository;

import com.infy.EcommerceUserManagement.entity.SellerEntity;


public interface SellerRepository extends CrudRepository<SellerEntity,Integer>{
		public SellerEntity findBySellerId(int Id);

		public SellerEntity findByEmail(String email);

		public SellerEntity findByPhoneNumber(String phoneNumber);
		
}

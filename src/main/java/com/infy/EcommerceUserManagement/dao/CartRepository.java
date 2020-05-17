package com.infy.EcommerceUserManagement.dao;








import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.infy.EcommerceUserManagement.entity.CartEntity;
import com.infy.EcommerceUserManagement.entity.CartId;

@Repository
public interface CartRepository extends CrudRepository<CartEntity, CartId>{
	

	
}
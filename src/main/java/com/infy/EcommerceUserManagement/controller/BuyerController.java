package com.infy.EcommerceUserManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@RestController
@PropertySource("classpath:configuration.properties")
public class BuyerController {

	@Autowired
	BuyerService buyerservice;

	@Autowired
	Environment environment;

	private static final Logger LOGGER = LoggerFactory.getLogger(BuyerController.class);

	@PostMapping(value = "api/buyer/register")
	public ResponseEntity<String> registerBuyer(@RequestBody Buyer buyer) {
		ResponseEntity<String> responseEntity = null;

		try {
			
			LOGGER.info("Buyer Registration is being done by "+buyer.getName());
			buyer.setActive(true);
			buyer.setPrivileged(false);
			buyer.setRewardPoints(0);
			buyerservice.buyerRegisterion(buyer);
			String successMessage = environment.getProperty("BuyerRegistration.REGISTRATION_SUCCESS");
			responseEntity = new ResponseEntity<String>(successMessage, HttpStatus.OK);

		} catch (Exception exception) {
			LOGGER.error("Error: " + exception.getMessage(), exception);

			String errorMessage;

			if (exception.getMessage() == null) {
				errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			} else {
				errorMessage = environment.getProperty(exception.getMessage());

				if (errorMessage == null)
					errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			}
		}

		return responseEntity;
	}

	@PostMapping(value = "api/buyer/login")
	public ResponseEntity<String> buyerLogin(@RequestBody Buyer buyer) {

		ResponseEntity<String> responseEntity = null;

		try {
			
			
			buyerservice.buyerLogin(buyer);
			String successMessage = environment.getProperty("BuyerLogin.LOGIN_SUCCESS");
			responseEntity = new ResponseEntity<String>(successMessage, HttpStatus.OK);

		} catch (Exception exception) {
			LOGGER.error("Error: " + exception.getMessage(), exception);

			String errorMessage;

			if (exception.getMessage() == null) {
				errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			} else {
				errorMessage = environment.getProperty(exception.getMessage());

				if (errorMessage == null)
					errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			}
		}

		return responseEntity;
	}

	

	@PostMapping(value = "api/buyer/deactivate")
	public ResponseEntity<String> deactivateBuyer(@RequestBody Buyer buyer) {

		ResponseEntity<String> responseEntity = null;

		try {
			
			
			buyerservice.deactivateBuyer(buyer);
			String successMessage = environment.getProperty("Buyer.DEACTIVATE_SUCCESS");
			responseEntity = new ResponseEntity<String>(successMessage, HttpStatus.OK);

		} catch (Exception exception) {
			LOGGER.error("Error: " + exception.getMessage(), exception);

			String errorMessage;

			if (exception.getMessage() == null) {
				errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			} else {
				errorMessage = environment.getProperty(exception.getMessage());

				if (errorMessage == null)
					errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			}
		}

		return responseEntity;
	}
	
	
	@PutMapping(value = "api/buyer/privilege")
	public ResponseEntity<String> updateBuyerPrivilege(@RequestParam(name = "email") String email ,@RequestParam(name = "privilege") boolean privilege) {

		ResponseEntity<String> responseEntity = null;

		try {
			
			
			buyerservice.updateBuyerPrivilege(email, privilege);
			String successMessage = environment.getProperty("Buyer.PRIVILEGE_UPDATE_SUCCESS");
			responseEntity = new ResponseEntity<String>(successMessage, HttpStatus.OK);

		} catch (Exception exception) {
			LOGGER.error("Error: " + exception.getMessage(), exception);

			String errorMessage;

			if (exception.getMessage() == null) {
				errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			} else {
				errorMessage = environment.getProperty(exception.getMessage());

				if (errorMessage == null)
					errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			}
		}

		return responseEntity;

	}
	
	
	
	
	
	@GetMapping(value = "api/rewardPoint")
	public int getRewardPoints(@RequestParam(name = "buyerId") int buyerId) {

		
		return buyerservice.getRewardPoint(buyerId);

	}

	@PutMapping(value = "api/rewardPoint/update")
	public void updateRewardPoint(@RequestParam(name = "buyerId") int buyerId,@RequestParam(name = "point") int point) {
		buyerservice.updateRewardPoint(buyerId, point);

	}

	@GetMapping(value = "api/buyer/isPrivilege")
	public boolean isBuyerPrivileged(@RequestParam(name = "buyerId") int buyerId) {

		return buyerservice.IsPrivileged(buyerId);
	}

	

	@GetMapping(value = "api/buyer")
	public ResponseEntity<String> getBuyerDetails(@RequestParam(name = "email") String email) {
		
		ResponseEntity<String> responseEntity = null;

		try {
			
			
			
			String successMessage =buyerservice.getBuyerDetails(email).toString();
			responseEntity = new ResponseEntity<String>(successMessage, HttpStatus.OK);

		} catch (Exception exception) {
			LOGGER.error("Error: " + exception.getMessage(), exception);

			String errorMessage;

			if (exception.getMessage() == null) {
				errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			} else {
				errorMessage = environment.getProperty(exception.getMessage());

				if (errorMessage == null)
					errorMessage = environment.getProperty("General.EXCEPTION");
				responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.OK);
				return responseEntity;

			}
		}

		return responseEntity;
		
		

	}

}

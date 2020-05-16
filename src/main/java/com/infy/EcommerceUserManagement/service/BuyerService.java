package com.infy.EcommerceUserManagement.service;

import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.infy.EcommerceUserManagement.controller.BuyerController;
import com.infy.EcommerceUserManagement.dao.BuyerRepository;
import com.infy.EcommerceUserManagement.entity.BuyerEntity;
import com.infy.EcommerceUserManagement.exception.AlreadyEmailIdExistException;
import com.infy.EcommerceUserManagement.exception.AlreadyNotPrivilegeException;
import com.infy.EcommerceUserManagement.exception.AlreadyPhoneNumberExistException;
import com.infy.EcommerceUserManagement.exception.AlreadyPrivilegeException;
import com.infy.EcommerceUserManagement.exception.InSufficientRewardPoint;
import com.infy.EcommerceUserManagement.exception.InvalidEmailIdException;
import com.infy.EcommerceUserManagement.exception.InvalidNameException;
import com.infy.EcommerceUserManagement.exception.InvalidPasswordException;
import com.infy.EcommerceUserManagement.exception.InvalidPhoneNumberException;
import com.infy.EcommerceUserManagement.exception.UserException;
import com.infy.EcommerceUserManagement.exception.WrongEmailException;
import com.infy.EcommerceUserManagement.exception.WrongPasswordException;
import com.infy.EcommerceUserManagement.model.Buyer;

@Service
@PropertySource(value = { "classpath:configuration.properties" })
public class BuyerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BuyerService.class);
	@Autowired
	private Environment env;
	@Autowired
	BuyerRepository buyerRepository;

	public void buyerRegisterion(Buyer buyer) throws Exception {
		LOGGER.info("Buyer details are going to validate");
		validateBuyer(buyer);
		LOGGER.info("Buyer details are validated successfully");
		BuyerEntity buyerEntity = new BuyerEntity();
		BeanUtils.copyProperties(buyer, buyerEntity);
		buyerRepository.save(buyerEntity);
		LOGGER.info("Buyer details are saved in DB successfully");
		
	}

	private void validateBuyer(Buyer buyer) throws Exception {
		LOGGER.info("Buyer details are being validated");
		// TODO Auto-generated method stub
		if(!isValidName(buyer.getName()))
			throw new InvalidNameException("BuyerRegistration.INVALID_NAME");
		if(!isValidEmail(buyer.getEmail()))
			throw new InvalidEmailIdException("BuyerRegistration.INVALID_EMAIL");
		if(!isValidPhoneNumber(buyer.getPhoneNumber()))
			throw new InvalidPhoneNumberException("BuyerRegistration.INVALID_PHONENUMBER");
		if(!isvalidPassword(buyer.getPassword()))
			throw new InvalidPasswordException("BuyerRegistration.INVALID_PASSWORD");
		if(!isAlreadyPhoneNumberExist(buyer.getPhoneNumber()))
			throw new AlreadyPhoneNumberExistException("BuyerRegistration.ALREADY_PH_EXISTS");
		if(!isAlreadyEmailIdExist(buyer.getEmail()))
			throw new AlreadyEmailIdExistException("BuyerRegistration.ALREADY_EMAIL_EXISTS");
		
		
	}

	private boolean isAlreadyEmailIdExist(String email) {
		// TODO Auto-generated method stub
		BuyerEntity buyerEntity=buyerRepository.findByEmail(email);
		if (buyerEntity!=null)
			return false;
		return true;
	}

	private boolean isAlreadyPhoneNumberExist(String phoneNumber) {
		// TODO Auto-generated method stub
		BuyerEntity buyerEntity=buyerRepository.findByPhoneNumber(phoneNumber);
		if (buyerEntity!=null)
			return false;
		return true;
	}

	private boolean isvalidPassword(String password) {
		// TODO Auto-generated method stub
		return Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{7,20}$",password);
	}

	private boolean isValidPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		return Pattern.matches("^\\d{10}$", phoneNumber);
	}

	private boolean isValidEmail(String email) {
		// TODO Auto-generated method stub
		return Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$",email);
	}

	private boolean isValidName(String name) {
		// TODO Auto-generated method stub
		return Pattern.matches("^[a-zA-Z]+[-a-zA-Z\\s]+([-a-zA-Z]+)$", name);
	}
	
	

	public void buyerLogin(Buyer buyer) throws UserException {

		BuyerEntity buyerEntity = buyerRepository.findByEmail(buyer.getEmail());
		if (buyerEntity != null) {
			if (buyerEntity.getPassword().equals(buyer.getPassword())) {
				
			} else {
				throw new WrongPasswordException("BuyerLogin.INVALID_PASSWORD");
			}

		} else {
			throw new WrongEmailException("BuyerLogin.INVALID_EMAILID");
		}

	}

	public void deactivateBuyer(Buyer buyer) throws UserException {

		BuyerEntity buyerEntity = buyerRepository.findByEmail(buyer.getEmail());
		if (buyerEntity != null) {
			buyerEntity.setActive(false);
			buyerRepository.save(buyerEntity);
			

		} else {
			throw new InvalidEmailIdException("Buyer.INVALID_EMAIL");
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
	
	public void updateBuyerPrivilege(String email,boolean privilege) throws UserException {
		BuyerEntity buyerEntity= buyerRepository.findByEmail(email);
		if(buyerEntity!=null){
			if(buyerEntity.isPrivileged()) {
				if(!privilege) {
					buyerEntity.setPrivileged(privilege);
				    buyerRepository.save(buyerEntity);
					
				}
				else {
				throw new AlreadyPrivilegeException("Buyer.ALREADY_PRIVILEGE");
				}
			}
			else if(!(buyerEntity.isPrivileged())) {
				if(privilege) {
					if(buyerEntity.getRewardPoints()<10000) {
						throw new InSufficientRewardPoint("Buyer.INSUFFICIENT_REWARD_POINTS");
					}
					else {
						buyerEntity.setPrivileged(privilege);
						buyerEntity.setRewardPoints(buyerEntity.getRewardPoints()-10000);
						buyerRepository.save(buyerEntity);
					}
					
				}
				else {
					
					throw new AlreadyNotPrivilegeException("Buyer.ALREADY_NOT_PRIVILEGE");
					
				}
			}
		buyerEntity.setPrivileged(privilege);
	    buyerRepository.save(buyerEntity);
		}
		else {
			throw new InvalidEmailIdException("Buyer.INVALID_EMAIL");
		}
		
	}
	
	public Buyer getBuyerDetails(String email) throws UserException {
		BuyerEntity buyerEntity= buyerRepository.findByEmail(email);
		if(buyerEntity!=null) {
		Buyer buyer=new Buyer();
		BeanUtils.copyProperties(buyerEntity, buyer);
		return buyer;
		}
		else {
			throw new InvalidEmailIdException("Buyer.INVALID_EMAIL");
		}
		
	}
	
	
	
	
	
	
	
}

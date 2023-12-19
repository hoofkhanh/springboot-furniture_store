package com.hokhanh.libary.service;

import org.springframework.web.multipart.MultipartFile;

import com.hokhanh.libary.dto.CustomerDto;
import com.hokhanh.libary.model.Customer;

public interface CustomerService {

	Customer findByUsername(String username);
	
	Customer save(CustomerDto customerDto);
	
	Customer update(Customer customer, MultipartFile customerImage);
	
	void updateResetPasswordToken(String token, String email);
	
	Customer getByResetPasswordToken(String token);
	
	void upadtePassword(Customer customer, String newPassword) ;
	
	Customer findByPassword(String password, String email);
		
	
	
}

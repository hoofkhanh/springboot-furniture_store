package com.hokhanh.libary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hokhanh.libary.dto.CustomerDto;
import com.hokhanh.libary.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Customer findByUsername(String username);
	
	Customer findByPhoneNumber(String phongNumber);
	
	Customer findByAccountNumber(String accountNumber);
	
	Customer findByResetPasswordToken(String token);
	
	Customer findByPassword(String password);
}

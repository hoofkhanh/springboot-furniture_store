package com.hokhanh.libary.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hokhanh.libary.dto.CustomerDto;
import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.model.Role;
import com.hokhanh.libary.repository.CustomerRepository;
import com.hokhanh.libary.repository.RoleRepository;
import com.hokhanh.libary.service.CustomerService;
import com.hokhanh.libary.utils.ImageUpload;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ImageUpload imageUpload;

	@Override
	public Customer findByUsername(String username) {
		return customerRepository.findByUsername(username);
	}

	@Override
	public Customer save(CustomerDto customerDto) {
		Customer customer = new Customer();
		customer.setFirstName(customerDto.getFirstName());
		customer.setLastName(customerDto.getLastName());
		customer.setUsername(customerDto.getUsername());
		customer.setPassword(bCryptPasswordEncoder.encode(customerDto.getPassword()));
		
		List<Role> roles = new ArrayList<>();
		roles.add(this.roleRepository.findByName("CUSTOMER"));
		customer.setRoleList(roles);
		
		return this.customerRepository.save(customer);
	}

	@Override
	public Customer update(Customer customer, MultipartFile customerImage) {
		
		try {
			Customer temp_phoneNumber = this.customerRepository.findByPhoneNumber(customer.getPhoneNumber());
			
			Customer temp_accountNumber = this.customerRepository.findByAccountNumber(customer.getAccountNumber());
			if(temp_phoneNumber != null && temp_phoneNumber.getId() != customer.getId()) {
				temp_phoneNumber.setPhoneNumber(null);
				return temp_phoneNumber;
			}else if(temp_accountNumber != null && temp_accountNumber.getId() != customer.getId()) {
				temp_accountNumber.setAccountNumber(null);
				return temp_accountNumber;
			}else {
				if(customerImage.isEmpty() == false) {
					imageUpload.uploadImageCustomer(customerImage);
					customer.setImage(Base64.getEncoder().encodeToString(customerImage.getBytes()));
				}else {
					Customer temp = this.customerRepository.findByUsername(customer.getUsername());
					customer.setImage(temp.getImage());
				}			
				
				return this.customerRepository.save(customer);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException  {
		Customer customer = this.customerRepository.findByUsername(email);
		if(customer != null) {
			customer.setResetPasswordToken(token);
			this.customerRepository.save(customer);
		}else {
			throw new UsernameNotFoundException("không tìm thấy bất kì khách hàng nào với email này "+ email);
		}
	}

	@Override
	public Customer getByResetPasswordToken(String token) {
		return this.customerRepository.findByResetPasswordToken(token);
	}

	@Override
	public void upadtePassword(Customer customer, String newPassword) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String encodePassword = bCryptPasswordEncoder.encode(newPassword);
		customer.setPassword(encodePassword);
		
		customer.setResetPasswordToken(null);
		this.customerRepository.save(customer);
	}

	@Override
	public Customer findByPassword(String password, String email) {
		Customer customer = this.customerRepository.findByUsername(email);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		if (encoder.matches(password, customer.getPassword())) {
		    return customer;
		} else {
		    return null;
		}
	}

	

}

package com.hokhanh.libary.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.model.Order;
import com.hokhanh.libary.model.Product;
import com.hokhanh.libary.model.ShoppingCart;

public interface OrderService {
	
//	customer

	void saveOrder(ShoppingCart shoppingCart, String paymentMethod);
	
	void cancelOrder(Long id) ;
	
	List<Order>  findAll();
	
	List<Order> findByCustomer(Customer customer);
		
//admin
	
	
	Page<Order> orderPage(int pageNo);

	Page<Order> searchOrders(String keyword, int pageNo);

	void acceptOrder(Long id);

	void refuseById(Long id);
	
	public List<Object[]> findAllToTalPriceOfOrderByMonth();
}

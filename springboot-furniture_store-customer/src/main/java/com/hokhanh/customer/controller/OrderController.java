package com.hokhanh.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hokhanh.libary.model.CartItem;
import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.model.Order;
import com.hokhanh.libary.model.Product;
import com.hokhanh.libary.model.ShoppingCart;
import com.hokhanh.libary.service.CustomerService;
import com.hokhanh.libary.service.OrderService;
import com.hokhanh.libary.service.ProductService;

@Controller
public class OrderController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;

	public void checkTotalItemOfCustomer(Authentication authentication, Model model) {
		if (authentication != null) {
			Customer customer = this.customerService.findByUsername(authentication.getName());
			ShoppingCart shoppingCart = customer.getShoppingCarts();
			if (shoppingCart != null) {
				model.addAttribute("totalItems", shoppingCart.getTotalItems());
			}
		}
	}

	@GetMapping("/checkout")
	public String checkout(Model model, Authentication authentication) {
		model.addAttribute("title", "Check out");
		Customer customer = this.customerService.findByUsername(authentication.getName());
		if (customer.getAddress() == null || customer.getCity() == null || customer.getPhoneNumber() == null || customer.getGender() == null
				|| customer.getNameOfBank() == null || customer.getAccountNumber() == null || customer.getBirthDay() == null
				|| customer.getNameOfBank().trim().isEmpty() || customer.getAccountNumber().trim().isEmpty()
				|| customer.getAddress().trim().isEmpty() || customer.getPhoneNumber().trim().isEmpty() || customer.getGender().trim().isEmpty() ) {
			model.addAttribute("customer", customer);
			return "redirect:/account";
		}else if(customer.getShoppingCarts() == null || customer.getShoppingCarts().getCartItems() == null || customer.getShoppingCarts().getCartItems() == null || customer.getShoppingCarts().getCartItems().isEmpty()) {
			return "redirect:/cart";
		}else {
			model.addAttribute("customer", customer);
		}

		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);

		checkTotalItemOfCustomer(authentication, model);
		return "checkout";
	}
	
	@GetMapping("/order")
	public String order(Model model, Authentication authentication) {
		model.addAttribute("title", "Order Page");
		
		Customer customer = this.customerService.findByUsername(authentication.getName());
		List<Order> orders = customer.getOrders();
		model.addAttribute("orderList", orders);
		
		for (Order or : orders) {
			if(or.getOrderStatus().equals("Đang chờ xác nhận") ){
				model.addAttribute("dangChoXacNhan", "dangChoXacNhan");
			}
		}
		
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);

		checkTotalItemOfCustomer(authentication, model);
		return "order";
	}
	
	@GetMapping("/transport")
	public String transport(Model model, Authentication authentication) {
		model.addAttribute("title", "Transport Page");
		
		Customer customer = this.customerService.findByUsername(authentication.getName());
		List<Order> orders = customer.getOrders();
		model.addAttribute("orderList", orders);
		
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);
		
		for (Order or : orders) {
			if(or.getOrderStatus().equals("Đang Ship") ){
				model.addAttribute("dangShip", "dangShip");
			}
		}

		checkTotalItemOfCustomer(authentication, model);
		return "transport";
	}
	
	@PostMapping("/saveOrder")
	public String saveOrder(Authentication authentication, String paymentMethod) {
		Customer customer = this.customerService.findByUsername(authentication.getName());
		ShoppingCart shoppingCart = customer.getShoppingCarts();
		this.orderService.saveOrder(shoppingCart, paymentMethod);
		return "redirect:/order";
	}
	
	@GetMapping("/cancelOrder")
	@ResponseBody
	public String cancelOrder(Long id) {
		this.orderService.cancelOrder(id);
		return "cancel";
	}
	
	@GetMapping("/checkSizeOrder")
	@ResponseBody
	public String findAll(Authentication authentication) {
		Customer customer = this.customerService.findByUsername(authentication.getName());
		List<Order> orders = this.orderService.findByCustomer(customer);
		
		for (Order order : orders) {
			if(order.getOrderStatus().equalsIgnoreCase("Đang chờ xác nhận")) {
				return "not null";
			}
		}
		
		
		return "null";
	}
	
}

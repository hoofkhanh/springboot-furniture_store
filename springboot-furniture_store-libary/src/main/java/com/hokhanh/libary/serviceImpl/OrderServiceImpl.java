package com.hokhanh.libary.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;import org.aspectj.asm.internal.ProgramElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hokhanh.libary.model.CartItem;
import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.model.Order;
import com.hokhanh.libary.model.Product;
import com.hokhanh.libary.model.ShoppingCart;
import com.hokhanh.libary.repository.CartItemRepository;
import com.hokhanh.libary.repository.OrderRepository;
import com.hokhanh.libary.repository.ProductRepository;
import com.hokhanh.libary.repository.ShoppingCartRepository;
import com.hokhanh.libary.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired 
	private CartItemRepository cartItemRepository;
	
	@Autowired 
	private ShoppingCartRepository shoppingCartRepository ;
	
	@Autowired 
	private ProductRepository productRepository ;
	
	


	@Override
	public void saveOrder(ShoppingCart shoppingCart, String paymentMethod) {
		Order order = new Order();
		order.setAccept(false);
		order.setCustomer(shoppingCart.getCustomer());
		
		LocalDate currentDate = LocalDate.now();
		LocalDate newDate = currentDate.plusDays(2);
		order.setDeliveryDate(newDate);
		
		order.setOrderDate(LocalDate.now());		
		order.setOrderStatus("Đang chờ xác nhận");
		order.setPaymentMethod(paymentMethod);
		order.setQuantity(shoppingCart.getTotalItems());
		

		double newTotalPrice__ = Math.round((shoppingCart.getTotalPrice()+ 30)*100.0)/100.0;	
		order.setTotalPrice(newTotalPrice__);
		
		
		this.orderRepository.save(order);
		
		List<CartItem> cartItems = shoppingCart.getCartItems();
		List<CartItem> cartItemAfterOrder = new ArrayList<>();
		for (CartItem cartItem : cartItems) {
			cartItem.setOrder(order);
			this.cartItemRepository.save(cartItem);
			
			Product product = cartItem.getProduct();
			product.setCurrentQuantity(product.getCurrentQuantity() - cartItem.getQuantity());
			this.productRepository.save(product);
				
			cartItemAfterOrder.add(cartItem);
		}
		
		cartItems.removeAll(cartItemAfterOrder);
		shoppingCart.setCartItems(cartItems);
		shoppingCart.setTotalItems(shoppingCart.getTotalItems() - cartItemAfterOrder.size());
		
		double price = 0;
		for (CartItem cartItem : cartItemAfterOrder) {
			price += cartItem.getTotalPrice();
		}
		shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() - price);
		
		double newTotalPrice = Math.round(shoppingCart.getTotalPrice()*100.0)/100.0;
		shoppingCart.setTotalPrice(newTotalPrice);

		this.shoppingCartRepository.save(shoppingCart);
	}



	@Override
	public void cancelOrder(Long id) {
		Order order = this.orderRepository.findById(id).get();
		List<CartItem> cartItems = order.getCartItem();
		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();
			product.setCurrentQuantity(cartItem.getQuantity() + product.getCurrentQuantity());	
			this.productRepository.save(product);
		}
		
		this.orderRepository.deleteById(id);
	}



	@Override
	public List<Order> findAll() {
		return this.orderRepository.findAll();
	}


	@Override
	public Page<Order> orderPage(int pageNo) {
//		số trang là pageNo, còn số lượng pageSize là 5
		pageNo -= 1;
		Pageable pageable = PageRequest.of(pageNo, 3);
		Page<Order> orderPage = this.orderRepository.orderPage(pageable);
		return orderPage;
	}



	@Override
	public Page<Order> searchOrders(String keyword, int pageNo) {
		Pageable pageable = PageRequest.of(pageNo - 1, 3);

		double number;
		try {
			number = Double.parseDouble(keyword);
		} catch (Exception e) {
			number = -1000000;
		}
		

		Page<Order> proPage = this.orderRepository.searchOrders(keyword, number, pageable);
		return proPage;
	}



	@Override
	public void acceptOrder(Long id) {
		Order order= this.orderRepository.findById(id).get();
		order.setAccept(true);
		order.setOrderStatus("Đang Ship");
		List<CartItem> cartItems =  order.getCartItem();
		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();
			product.setSold(product.getSold() + cartItem.getQuantity());
			this.productRepository.save(product);
		}
		this.orderRepository.save(order);
	}



	@Override
	public void refuseById(Long id) {
		Order order= this.orderRepository.findById(id).get();
		order.setAccept(false);
		order.setOrderStatus("Đang chờ xác nhận");
		
		List<CartItem> cartItems =  order.getCartItem();
		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();
			product.setSold(product.getSold() - cartItem.getQuantity());
			this.productRepository.save(product);
		}
		this.orderRepository.save(order);
	}



	@Override
	public List<Order> findByCustomer(Customer customer) {
		return this.orderRepository.findByCustomer(customer);
	}

	@Override
	public List<Object[]> findAllToTalPriceOfOrderByMonth() {
		return this.orderRepository.sumTotalPriceByMonth();
	}

}

package com.hokhanh.libary.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.model.Order;
import com.hokhanh.libary.model.Product;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query(value = "SELECT * FROM orders", nativeQuery = true)
	Page<Order> orderPage(Pageable pageable);

	@Query(value = "SELECT DISTINCT o.order_id, o.delivery_date, o.is_accept, o.order_date, o.order_status, o.payment_method, o.quantity, o.total_price, o.customer_id "
			+ "FROM orders o INNER JOIN cart_items c ON c.order_id = o.order_id "
			+ "WHERE o.delivery_date LIKE %:keyword% OR o.order_date LIKE %:keyword% OR "
			+ "o.customer_id IN (SELECT customer_id FROM customers WHERE CONCAT(last_name, ' ', first_name) LIKE %:keyword%) " 
			+ "OR o.payment_method LIKE %:keyword% OR o.order_status LIKE %:keyword% "
			+ "OR o.quantity = :number OR o.total_price = :number OR c.cart_items_id IN (SELECT c.cart_items_id FROM products p INNER JOIN cart_items c ON p.product_id = c.product_id  WHERE p.name LIKE %:keyword% OR p.current_quantity = :number OR p.cost_price = :number) ", nativeQuery = true)
	Page<Order> searchOrders(@Param("keyword") String keyword, @Param("number") double quantity, Pageable pageable);
	
	List<Order> findByCustomer(Customer customer);
	
	@Query(value = "SELECT MONTH(order_date), SUM(total_price) FROM orders WHERE YEAR(order_date) = YEAR(NOW()) AND order_status = 'Đang Ship' "
			+ "GROUP BY MONTH(order_date) ORDER BY MONTH(order_date) DESC", nativeQuery = true)
	List<Object[]> sumTotalPriceByMonth();
}

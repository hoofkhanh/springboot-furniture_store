package com.hokhanh.libary.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hokhanh.libary.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	//admin
	Product findByName(String name);
	
	@Query(value = "SELECT * FROM products WHERE image = :base64Image", nativeQuery = true)
	Product findByImageData(@Param("base64Image") String base64Image);
	
	@Query(value = "SELECT * FROM products", nativeQuery = true)
	Page<Product> productPage (Pageable pageable);	
	
	@Query(value = "SELECT * FROM products WHERE description LIKE %:keyword% OR name LIKE %:keyword% OR "
			+ "category_id IN (SELECT category_id FROM categories WHERE name LIKE %:keyword%)"
			+ "OR cost_price = :number OR current_quantity = :number", nativeQuery = true)
	Page<Product> searchProducts(@Param("keyword")String keyword,@Param("number") double number, Pageable pageable);
	
	// customer
		@Query(value = "SELECT * FROM products WHERE activated = true and hidden = false", nativeQuery = true)
		List<Product> getAllProduct();

//		@Query(value = "SELECT * FROM products WHERE activated = true and hidden = false ORDER BY RAND() LIMIT 4", nativeQuery = true)
//		List<Product> getListViewProduct();

		@Query(value = "SELECT * FROM products WHERE category_id = :category_id AND activated = TRUE AND hidden = FALSE", nativeQuery = true)
		List<Product> getRelatedProduct(@Param("category_id") Long category_id);
		
		
		@Query(value = "SELECT * FROM products WHERE activated = true AND hidden = false ORDER BY cost_price DESC", nativeQuery = true)
		List<Product> sort_getProduct_byHighToLowPrice_byAll();
		
		@Query(value = "SELECT * FROM products WHERE activated = true AND hidden = false ORDER BY cost_price", nativeQuery = true)
		List<Product> sort_getProduct_byLowToHighPrice_byAll();
		
		@Query(value = "SELECT * FROM products WHERE category_id = :category_id  AND activated = true AND hidden = false ORDER BY cost_price DESC", nativeQuery = true)
		List<Product> sort_getProduct_byHighToLowPrice_byCategory(@Param("category_id") Long category_id);
		
		@Query(value = "SELECT * FROM products WHERE category_id = :category_id AND activated = true AND hidden = false ORDER BY cost_price", nativeQuery = true)
		List<Product> sort_getProduct_byLowToHighPrice_byCategory(@Param("category_id") Long category_id);
		
		@Query(value = "SELECT * FROM products where activated = true AND hidden = false and sold > 0  ORDER BY sold DESC", nativeQuery = true)
		List<Product> findProductBestSeller_DESC_SOLD();
		
		@Query(value = "SELECT * FROM products WHERE category_id = :category_id AND activated = true AND hidden = false and sold>0 ORDER BY sold DESC", nativeQuery = true)
		List<Product> findProductBestSeller_DESC_SOLD_BY_CATE(@Param("category_id") Long category_id);
		
		@Query(value = "SELECT * FROM products WHERE category_id = :category_id AND activated = true AND hidden = false AND sold > 0 ORDER BY sold desc	 LIMIT 4", nativeQuery = true)
		List<Product> findProductBestSeller_menuPage(@Param("category_id") Long category_id);

		@Query(value = "SELECT * FROM products WHERE category_id = :category_id AND activated = true AND hidden = false AND sale_price >0 ORDER BY sale_price", nativeQuery = true)
		List<Product> findProduct_sale_byCate(@Param("category_id") Long category_id);
		
		@Query(value = "SELECT * FROM products WHERE activated = true AND hidden = false AND sale_price >0 ORDER BY sale_price", nativeQuery = true)
		List<Product> findProduct_sale();
		
		@Query(value = "SELECT * FROM products WHERE activated = true AND hidden = false AND name LIKE %:name% ORDER BY cost_price", nativeQuery = true)
		List<Product> findProduct_SearchByName(String name);
		
		
		
		
//		pageable admin
		
		@Query(value = "SELECT * FROM products WHERE activated =TRUE AND hidden = FALSE", nativeQuery = true)
		Page<Product> getProductsOnPageable(Pageable pageable);
		
		@Query(value = "SELECT * FROM products WHERE category_id = :category_id AND activated = TRUE AND hidden = FALSE", nativeQuery = true)
		Page<Product> getRelatedProductOnPageable(@Param("category_id") Long category_id, Pageable pageable);
		
		
		
		
}

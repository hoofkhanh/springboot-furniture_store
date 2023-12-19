package com.hokhanh.libary.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.hokhanh.libary.dto.ProductDto;
import com.hokhanh.libary.model.Product;

public interface ProductService {
	// Admin
	List<ProductDto> findAll();

	Product update(ProductDto productDto, MultipartFile imageProduct);

	Product save(ProductDto productDto, MultipartFile imageProduct);

	void deleteById(Long id);

	void activatedById(Long id);

	void hiddenById(Long id);

	Product findById(Long id);

	Page<Product> productPage(int pageNo);

	Page<Product> searchProducts(String keyword, int pageNo);
	
	
//	Customer
	
	
	List<Product> getAllProduct();
	
//	List<Product> getListViewProduct();
	
	List<Product> getRelatedProducts(Long category_id);
	
	List<Product> sort_getProduct_byHighToLowPrice_byAll();
	
	List<Product> sort_getProduct_byLowToHighPrice_byAll();
	
	List<Product> sort_getProduct_byHighToLowPrice_byCategory(Long id);
	
	List<Product> sort_getProduct_byLowToHighPrice_byCategory(Long id);
	
	List<Product> findProductBestSeller_DESC_SOLD();
	
	List<Product> findProductBestSeller_DESC_SOLD_BY_CATE(Long category_id);
	
	List<Product> findProductBestSeller_menuPage(Long category_id);
	
	
	List<Product> findProduct_saleByCate(Long category_id);
	
	List<Product> findProduct_sale();
	
	List<Product> findProduct_search(String name);
}

package com.hokhanh.libary.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hokhanh.libary.dto.ProductDto;
import com.hokhanh.libary.model.Product;
import com.hokhanh.libary.repository.ProductRepository;
import com.hokhanh.libary.service.ProductService;
import com.hokhanh.libary.utils.ImageUpload;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ImageUpload imageUpload;

	@Override
	public List<ProductDto> findAll() {
		List<ProductDto> productDtos = new ArrayList<>();
		List<Product> products = this.productRepository.findAll();

		for (Product product : products) {
			ProductDto productDto = new ProductDto();
			productDto.setId(product.getId());
			productDto.setName(product.getName());
			productDto.setDescription(product.getDescription());
			productDto.setCurrentQuantity(product.getCurrentQuantity());
			productDto.setCostPrice(product.getCostPrice());
			productDto.setSalePrice(product.getSalePrice());
			productDto.setImage(product.getImage());
			productDto.setCategory(product.getCategory());
			productDto.setActivated(product.isActivated());
			productDto.setHidden(product.isHidden());

			productDtos.add(productDto);
		}

		return productDtos;
	}

	@Override
	public Product update(ProductDto productDto, MultipartFile imageProduct) {
		Product product = new Product();
		try {

			Product temp = this.productRepository.findByName(productDto.getName());
			Product temp_2 = null;
			if (!imageProduct.isEmpty()) {
				String base = Base64.getEncoder().encodeToString(imageProduct.getBytes());
				temp_2 = this.productRepository.findByImageData(base);
			}

			if ((temp != null && temp.getId() != productDto.getId())
					|| (temp_2 != null && temp_2.getId() != productDto.getId())) {
				return null;
			} else {
				if (imageProduct.isEmpty()) {
					product.setImage(this.productRepository.findById(productDto.getId()).get().getImage());
				} else {
					imageUpload.uploadImage(imageProduct);
					product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
				}

				product.setId(productDto.getId());
				product.setName(productDto.getName());
				product.setCategory(productDto.getCategory());
				product.setCostPrice(productDto.getCostPrice());
				product.setCurrentQuantity(productDto.getCurrentQuantity());
				product.setActivated(productDto.isActivated());
				product.setHidden(productDto.isHidden());
				product.setDescription(productDto.getDescription());
				product.setSalePrice(productDto.getSalePrice());
				product.setSold(productDto.getSold());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.productRepository.save(product);
	}

	@Override
	public Product save(ProductDto productDto, MultipartFile imageProduct) {
		try {
			Product temp = this.productRepository.findByName(productDto.getName());
			Product temp_2 = null;
			if (!imageProduct.isEmpty()) {
				String base = Base64.getEncoder().encodeToString(imageProduct.getBytes());
				temp_2 = this.productRepository.findByImageData(base);
//				System.out.println(temp_2);
			}

			if (temp != null || temp_2 != null) {
				return null;
			} else {
				Product product = new Product();

				if (imageProduct.isEmpty()) {
					product.setImage(null);
				} else {
					this.imageUpload.uploadImage(imageProduct);
					product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
				}

				product.setName(productDto.getName());
				product.setDescription(productDto.getDescription());
				product.setCostPrice(productDto.getCostPrice());
				product.setCurrentQuantity(productDto.getCurrentQuantity());
				product.setCategory(productDto.getCategory());
				product.setActivated(true);
				product.setHidden(false);
				return this.productRepository.save(product);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteById(Long id) {
		try {
			this.productRepository.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void activatedById(Long id) {
		Product product = this.productRepository.findById(id).get();
		product.setActivated(true);
		product.setHidden(false);
		this.productRepository.save(product);
	}

	@Override
	public void hiddenById(Long id) {
		Product product = this.productRepository.findById(id).get();
		product.setActivated(false);
		product.setHidden(true);
		this.productRepository.save(product);
	}

	@Override
	public Product findById(Long id) {
		try {
			return this.productRepository.findById(id).orElse(null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<Product> productPage(int pageNo) {
//		số trang là pageNo, còn số lượng pageSize là 5
		pageNo -= 1;
		Pageable pageable = PageRequest.of(pageNo, 5);
		Page<Product> productPage = this.productRepository.productPage(pageable);
		return productPage;
	}

	@Override
	public Page<Product> searchProducts(String keyword, int pageNo) {
		Pageable pageable = PageRequest.of(pageNo - 1, 5);

		double number;
		try {
			number = Double.parseDouble(keyword);
		} catch (Exception e) {
			number = -1000000;
		}

		Page<Product> proPage = this.productRepository.searchProducts(keyword, number, pageable);
		return proPage;
	}

	public List<Product> getAllProduct(){
		return this.productRepository.getAllProduct();
	}
	
//	public List<Product> getListViewProduct(){
//		return this.productRepository.getListViewProduct();
//	}

	@Override
	public List<Product> getRelatedProducts(Long category_id) {
		return this.productRepository.getRelatedProduct(category_id);
	}


	@Override
	public List<Product> sort_getProduct_byHighToLowPrice_byAll() {
		return this.productRepository.sort_getProduct_byHighToLowPrice_byAll();
	}
	
	@Override
	public List<Product> sort_getProduct_byLowToHighPrice_byAll() {
		return this.productRepository.sort_getProduct_byLowToHighPrice_byAll();
	}

	@Override
	public List<Product> sort_getProduct_byHighToLowPrice_byCategory(Long id) {
		return this.productRepository.sort_getProduct_byHighToLowPrice_byCategory(id);
	}

	@Override
	public List<Product> sort_getProduct_byLowToHighPrice_byCategory(Long id) {
		return this.productRepository.sort_getProduct_byLowToHighPrice_byCategory(id);
	}

	@Override
	public List<Product> findProductBestSeller_DESC_SOLD() {
		return this.productRepository.findProductBestSeller_DESC_SOLD();
	}

	@Override
	public List<Product> findProductBestSeller_DESC_SOLD_BY_CATE(Long category_id) {
		return this.productRepository.findProductBestSeller_DESC_SOLD_BY_CATE(category_id);
	}

	@Override
	public List<Product> findProductBestSeller_menuPage(Long category_id) {
		return this.productRepository.findProductBestSeller_menuPage(category_id);
	}

	@Override
	public List<Product> findProduct_saleByCate(Long category_id) {
		return this.productRepository.findProduct_sale_byCate(category_id);
	}

	@Override
	public List<Product> findProduct_sale() {
		return this.productRepository.findProduct_sale();
	}

	@Override
	public List<Product> findProduct_search(String name) {
		return this.productRepository.findProduct_SearchByName(name);
	}

}

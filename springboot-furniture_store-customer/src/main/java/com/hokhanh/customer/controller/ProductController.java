package com.hokhanh.customer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hokhanh.libary.dto.CategoryDto;
import com.hokhanh.libary.dto.ProductDto;
import com.hokhanh.libary.model.CartItem;
import com.hokhanh.libary.model.Category;
import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.model.Product;
import com.hokhanh.libary.model.ShoppingCart;
import com.hokhanh.libary.service.CategoryService;
import com.hokhanh.libary.service.CustomerService;
import com.hokhanh.libary.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CustomerService customerService;

	public void checkTotalItemOfCustomer(Authentication authentication, Model model) {
		if (authentication != null) {
			Customer customer = this.customerService.findByUsername(authentication.getName());
			ShoppingCart shoppingCart = customer.getShoppingCarts();
			if (shoppingCart != null) {
				model.addAttribute("totalItems", shoppingCart.getTotalItems());
			}
		}
	}
	
	

	@GetMapping("/products")
	public String products(Model model, Authentication authentication) {
		model.addAttribute("title", "Menu");
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);
		
		List<Product> searchList = this.productService.getAllProduct();
		model.addAttribute("searchList", searchList);

		List<Product> listViewProduct = this.productService.getAllProduct();
		model.addAttribute("productList", products);
		model.addAttribute("listViewProduct", listViewProduct);

		List<CategoryDto> getCategoryAndProduct = this.categoryService.getCategoryAndProduct();
		model.addAttribute("categoryAndProductList", getCategoryAndProduct);

		model.addAttribute("totalOfProduct", products.size());

		checkTotalItemOfCustomer(authentication, model);
		return "shop";
	}
	
	@GetMapping("/products/bestSeller")
	public String productsBestSeller(Model model,@RequestParam(name = "category_id", defaultValue = "-1") Long category_id, Authentication authentication) {
		model.addAttribute("title", "Shop Detail");
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);
		
		List<Product> searchList = this.productService.getAllProduct();
		model.addAttribute("searchList", searchList);

		List<Product> listViewProduct = this.productService.findProductBestSeller_DESC_SOLD();
		if(category_id != -1) {
			listViewProduct= this.productService.findProductBestSeller_DESC_SOLD_BY_CATE(category_id);
		}
		
		model.addAttribute("productList", listViewProduct);
		model.addAttribute("listViewProduct", listViewProduct);

		List<CategoryDto> getCategoryAndProduct = this.categoryService.getCategoryAndProduct();
		model.addAttribute("categoryAndProductList", getCategoryAndProduct);

		model.addAttribute("totalOfProduct", products.size());
		
		model.addAttribute("category_id", category_id);

		checkTotalItemOfCustomer(authentication, model);
		return "shop";
	}
	
	@GetMapping("/products/sale")
	public String product_sale(Model model,@RequestParam(name = "category_id", defaultValue = "-1") Long category_id, Authentication authentication) {
		model.addAttribute("title", "Shop Detail");
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);
		
		List<Product> searchList = this.productService.getAllProduct();
		model.addAttribute("searchList", searchList);

		List<Product> listViewProduct = this.productService.findProduct_sale();
		if(category_id != -1) {
			listViewProduct= this.productService.findProduct_saleByCate(category_id);
		}
		
		model.addAttribute("productList", listViewProduct);
		model.addAttribute("listViewProduct", listViewProduct);

		List<CategoryDto> getCategoryAndProduct = this.categoryService.getCategoryAndProduct();
		model.addAttribute("categoryAndProductList", getCategoryAndProduct);

		model.addAttribute("totalOfProduct", products.size());
		
		model.addAttribute("category_id", category_id);

		checkTotalItemOfCustomer(authentication, model);
		return "shop";
	}

	@GetMapping("/products/byCategory")
	public String ProductByCategory(@RequestParam(name = "category_id", defaultValue = "-1") Long category_id,
			Model model, RedirectAttributes r, Authentication authentication) {
		model.addAttribute("title", "Menu");
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);
		
		List<Product> searchList = this.productService.getAllProduct();
		model.addAttribute("searchList", searchList);

		List<Product> listViewProduct = new ArrayList<>();
		List<Product> productListByCate = new ArrayList<>();
		if (category_id != -1) {
			listViewProduct = this.productService.getRelatedProducts(category_id);
			productListByCate = this.productService.getRelatedProducts(category_id);
		} else {
			return "redirect:/products";
		}

		model.addAttribute("listViewProduct", listViewProduct);

		model.addAttribute("productList", productListByCate);

		List<CategoryDto> getCategoryAndProduct = this.categoryService.getCategoryAndProduct();
		model.addAttribute("categoryAndProductList", getCategoryAndProduct);

		model.addAttribute("totalOfProduct", products.size());

		model.addAttribute("category_id", category_id);
		
		checkTotalItemOfCustomer(authentication, model);
		return "shop";
	}
	
	@GetMapping("/products/search")
	public String findProductSearchByName(Model model,String name, Authentication authentication) {
		model.addAttribute("title", "Menu");
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);
		
		List<Product> searchList = this.productService.getAllProduct();
		model.addAttribute("searchList", searchList);

		List<Product> productSearch =  this.productService.findProduct_search(name);

		List<Product> listViewProduct = this.productService.findProduct_search(name);
		model.addAttribute("productList", productSearch);
		model.addAttribute("listViewProduct", listViewProduct);

		List<CategoryDto> getCategoryAndProduct = this.categoryService.getCategoryAndProduct();
		model.addAttribute("categoryAndProductList", getCategoryAndProduct);

		model.addAttribute("totalOfProduct", products.size());

//		model.addAttribute("category_id", category_id);

		checkTotalItemOfCustomer(authentication, model);
		
		model.addAttribute("name", name);
		
		return "shop";
	}
	
	

	@GetMapping("/products/high-to-low-price")
	public String sortProducts_hightToLowPrice(Model model,
			@RequestParam(name = "category_id", defaultValue = "-1") Long category_id, Authentication authentication) {
		model.addAttribute("title", "Menu");
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);
		
		List<Product> searchList = this.productService.getAllProduct();
		model.addAttribute("searchList", searchList);

		List<Product> productList_sort_highToLowPrice = new ArrayList<>();
		if (category_id != -1) {
			productList_sort_highToLowPrice = this.productService
					.sort_getProduct_byHighToLowPrice_byCategory(category_id);
		} else {
			productList_sort_highToLowPrice = this.productService.sort_getProduct_byHighToLowPrice_byAll();
		}

		List<Product> listViewProduct = productList_sort_highToLowPrice;
		model.addAttribute("productList", productList_sort_highToLowPrice);
		model.addAttribute("listViewProduct", listViewProduct);

		List<CategoryDto> getCategoryAndProduct = this.categoryService.getCategoryAndProduct();
		model.addAttribute("categoryAndProductList", getCategoryAndProduct);

		model.addAttribute("totalOfProduct", products.size());

		model.addAttribute("category_id", category_id);

		checkTotalItemOfCustomer(authentication, model);
		return "shop";
	}

	@GetMapping("/products/low-to-high-price")
	public String sortProducts_lowToHighPrice(Model model,
			@RequestParam(name = "category_id", defaultValue = "-1") Long category_id, Authentication authentication) {
		model.addAttribute("title", "Menu");
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);
		
		List<Product> searchList = this.productService.getAllProduct();
		model.addAttribute("searchList", searchList);

		List<Product> productList_sort_highToLowPrice = new ArrayList<>();
		if (category_id != -1) {
			productList_sort_highToLowPrice = this.productService
					.sort_getProduct_byLowToHighPrice_byCategory(category_id);
		} else {
			productList_sort_highToLowPrice = this.productService.sort_getProduct_byLowToHighPrice_byAll();
		}

		List<Product> listViewProduct = productList_sort_highToLowPrice;
		model.addAttribute("productList", productList_sort_highToLowPrice);
		model.addAttribute("listViewProduct", listViewProduct);

		List<CategoryDto> getCategoryAndProduct = this.categoryService.getCategoryAndProduct();
		model.addAttribute("categoryAndProductList", getCategoryAndProduct);

		model.addAttribute("totalOfProduct", products.size());

		model.addAttribute("category_id", category_id);

		checkTotalItemOfCustomer(authentication, model);
		return "shop";
	}

	@GetMapping("/menu")
	public String menu(Model model, Authentication authentication) {

		model.addAttribute("title", "Menu");
		List<Product> products = new ArrayList<>();
		model.addAttribute("productFeed", products);
		
		

		List<Category> categories = categoryService.findAllByActivated();
		

		for (Category category : categories) {
			List<Product> productList = this.productService.findProductBestSeller_menuPage(category.getId());
			
			for (Product p : productList) {
				products.add(p);
			}
		}
		
		model.addAttribute("productList", products);
		model.addAttribute("categoryList", categories);
		
		checkTotalItemOfCustomer(authentication, model);
		return "index";
	}

	@GetMapping("/find-productById")
	public String productDetail(Long id, Model model, Authentication authentication) {
		model.addAttribute("title", "Product Detail");
		List<Product> products = this.productService.getAllProduct();
		model.addAttribute("productFeed", products);

		Product product = this.productService.findById(id);
		model.addAttribute("product", product);

		List<Product> relatedProductList = this.productService.getRelatedProducts(product.getCategory().getId());
		relatedProductList.remove(product);
		model.addAttribute("relatedProductList", relatedProductList);
	
		int maxQuantity =-1;
		int minQuantity = -1;
		int valueQuantity =-1;
		if(authentication != null) {
			Customer customer = this.customerService.findByUsername(authentication.getName());
			ShoppingCart shoppingCart = customer.getShoppingCarts();
			if(shoppingCart != null) {
				for (CartItem c : shoppingCart.getCartItems()) {
					if(c.getProduct().getId() == product.getId()) {
						maxQuantity = product.getCurrentQuantity() - c.getQuantity();
						if(maxQuantity ==0) {
							minQuantity =0;
							valueQuantity =0;
						}else {
							minQuantity= 1;
							valueQuantity=1;
						}
					}
				}
			}
			
			if(maxQuantity == -1) {
				maxQuantity = product.getCurrentQuantity();
				minQuantity =1;
				valueQuantity =1;
			}
		}else {
			maxQuantity = product.getCurrentQuantity();
			minQuantity =1;
			valueQuantity=1;
		}
		model.addAttribute("maxQuantity", maxQuantity);
		model.addAttribute("minQuantity", minQuantity);
		model.addAttribute("valueQuantity", valueQuantity);

		checkTotalItemOfCustomer(authentication, model);
		return "product-detail";
	}

}

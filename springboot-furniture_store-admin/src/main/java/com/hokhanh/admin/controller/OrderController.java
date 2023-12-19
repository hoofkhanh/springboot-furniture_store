package com.hokhanh.admin.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hokhanh.libary.dto.ProductDto;
import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.model.Order;
import com.hokhanh.libary.model.Product;
import com.hokhanh.libary.repository.CategoryRepository;
import com.hokhanh.libary.service.CategoryService;
import com.hokhanh.libary.service.CustomerService;
import com.hokhanh.libary.service.OrderService;
import com.hokhanh.libary.service.ProductService;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerService customerService;

	@GetMapping("/orders")
	public String orders(Model m) {
		m.addAttribute("title", "ORDER");
		m.addAttribute("orderList", orderService.findAll());
		return "orders";
	}

	@GetMapping("/orders/{pageNo}")
	public String ordersPage(@PathVariable("pageNo") int pageNo, Model m) {
		
		Page<Order> orderList = this.orderService.orderPage(pageNo);
		
		
		if(orderList.isEmpty() && pageNo != 1) {
			orderList = this.orderService.orderPage(pageNo-1);
			return "redirect:/orders/"+(pageNo -1);
		}

		m.addAttribute("title", "Order");
		m.addAttribute("totalPage", orderList.getTotalPages());
		m.addAttribute("currentPage", pageNo);

		m.addAttribute("orderList", orderList);
		return "orders";
	}

//	@PostMapping("/products/insert")
//	public String insertProduct(ProductDto productDto, @RequestParam("imageProduct") MultipartFile imageProduct,
//			RedirectAttributes redirectAttributes,
//			@RequestParam(name = "totalPage", defaultValue = "1") int totalPage) {
//
//		if(totalPage ==0 ) {
//			totalPage = 1;
//		}
//		
//		Product product = this.productService.save(productDto, imageProduct);
//		try {
//			if (product != null) {
//				redirectAttributes.addFlashAttribute("success", "Insert Successfully");
//				redirectAttributes.addFlashAttribute("insert", "Insert Successfully");
//			} else {
//				redirectAttributes.addFlashAttribute("failed", "Insert Failed because Duplicate ");
//			}
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("failed", "Insert Failed because Duplicate ");
//		}
//		return "redirect:/products/" + totalPage;
//
//	}

//	@GetMapping("products/getById")
//	@ResponseBody
//	public Product getById(Long id) {
//		return this.productService.findById(id);
//	}

//	@PostMapping(value = "/products/update")
//	public String updateProduct(ProductDto productDto, @RequestParam("imageProduct") MultipartFile imageProduct,
//			RedirectAttributes redirectAttributes, int currentPage) {
//		Product product = this.productService.update(productDto, imageProduct);
//		try {
//			if (product != null) {
//				redirectAttributes.addFlashAttribute("success", "Update Successfully");
//			} else {
//				redirectAttributes.addFlashAttribute("failed", "Update Failed because Duplicate ");
//			}
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("failed", "Update Failed");
//		}
//		return "redirect:/products/" + currentPage;
//	}

//	@GetMapping("/products/hiddenById")
//	public String hiddenById(Long id, RedirectAttributes redirectAttributes, int currentPage) {
//		try {
//			this.productService.hiddenById(id);
//			redirectAttributes.addFlashAttribute("success", "Hidden Successfully");
//		} catch (Exception e) {
//			e.printStackTrace();
//			redirectAttributes.addFlashAttribute("failed", "Hidden Failed");
//		}
//		return "redirect:/products/" + currentPage;
//	}

//	@GetMapping("/products/activatedById")
//	public String activateProduct(Long id, RedirectAttributes redirectAttributes, int currentPage) {
//		try {
//			this.productService.activatedById(id);
//			redirectAttributes.addFlashAttribute("success", "Activated Successfully");
//		} catch (Exception e) {
//			e.printStackTrace();
//			redirectAttributes.addFlashAttribute("failed", "Activated Failed");
//		}
//		return "redirect:/products/" + currentPage;
//	}

	@GetMapping("/orders/deleteById")
	public String deleteProduct(Long id, RedirectAttributes redirectAttributes, int currentPage) {
		try {
			this.orderService.cancelOrder(id);
			redirectAttributes.addFlashAttribute("success", "Delete Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("failed", "Delete Failed");
		}
		return "redirect:/orders/" + currentPage;
	}

	@GetMapping("/orders/search")
	public String searchProducts(Model m, String keyword, @RequestParam(name = "pageNo") int pageNo) {
		try {
			if (keyword == "") {
				return "redirect:/orders/1";
			} else {
				Page<Order> orderList = this.orderService.searchOrders(keyword, pageNo);
				
				int totalPage = orderList.getTotalPages();
				List<Order> orderListNotNull = new ArrayList<>();
				for(int i=1; i<= totalPage; i++) {
					Page<Order> temp = this.orderService.searchOrders(keyword, i);
					orderListNotNull.addAll(temp.getContent());
				}
				
				Pageable pageable = PageRequest.of(pageNo - 1, 3);
				final int start = (int)pageable.getOffset();
				final int end = Math.min((start + pageable.getPageSize()), orderListNotNull.size());
				Page<Order> orderListNotNullPage = new PageImpl<>(orderListNotNull.subList(start, end), pageable, orderListNotNull.size());
				
				
				m.addAttribute("orderList", orderListNotNullPage);
				m.addAttribute("keyword", keyword);

				m.addAttribute("title", "Order");
				m.addAttribute("totalPage", orderListNotNullPage.getTotalPages());

//		trang bắt đầu là 0
				m.addAttribute("currentPage", orderListNotNullPage.getNumber() + 1);

				m.addAttribute("searchProduct", "search Product");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("failed", "Search Failed");
		}
		return "orders";
	}

//	@GetMapping("/products/search/hiddenById")
//	public String hiddenSearchProduct(Long id, Model m, String keyword, @RequestParam(name = "pageNo") int pageNo) {
//		try {
//			Page<Product> productList = this.productService.searchProducts(keyword, pageNo);
//			this.productService.hiddenById(id);
//
//			m.addAttribute("productList", productList);
//			m.addAttribute("keyword", keyword);
//
//			m.addAttribute("title", "PRODUCT");
//			m.addAttribute("totalPage", productList.getTotalPages());
//
////	trang bắt đầu là 0
//			m.addAttribute("currentPage", productList.getNumber() + 1);
//			m.addAttribute("categoryList", categoryService.findAllByActivated());
//
//			m.addAttribute("searchProduct", "search Product");
//			m.addAttribute("success", "Hidden Successfully");
//		} catch (Exception e) {
//			e.printStackTrace();
//			m.addAttribute("failed", "Hidden Failed");
//		}
//		return "products";
//	}

//	@GetMapping("/products/search/activatedById")
//	public String activatedSearchProduct(Long id, Model m, String keyword, @RequestParam(name = "pageNo") int pageNo) {
//		try {
//			Page<Product> productList = this.productService.searchProducts(keyword, pageNo);
//			this.productService.activatedById(id);
//
//			m.addAttribute("productList", productList);
//			m.addAttribute("keyword", keyword);
//
//			m.addAttribute("title", "PRODUCT");
//			m.addAttribute("totalPage", productList.getTotalPages());
//
////		trang bắt đầu là 0
//			m.addAttribute("currentPage", productList.getNumber() + 1);
//			m.addAttribute("categoryList", categoryService.findAllByActivated());
//
//			m.addAttribute("searchProduct", "search Product");
//			m.addAttribute("success", "Activated Successfully");
//		} catch (Exception e) {
//			e.printStackTrace();
//			m.addAttribute("failed", "Activated Failed");
//		}
//		return "products";
//	}

	@GetMapping("/orders/search/deleteById")
	public String deleteSearchProduct(Long id, Model m, String keyword, @RequestParam(name = "pageNo") int pageNo) {
		try {
			this.orderService.cancelOrder(id);
			Page<Order> orderList = this.orderService.searchOrders(keyword, pageNo);

			m.addAttribute("orderList", orderList);
			m.addAttribute("keyword", keyword);

			m.addAttribute("title", "Order");
			m.addAttribute("totalPage", orderList.getTotalPages());
			
		
//		trang bắt đầu là 0
			if(orderList.isEmpty() == false ) {
				m.addAttribute("currentPage", orderList.getNumber() + 1);

				m.addAttribute("searchProduct", "search Product");
				m.addAttribute("success", "Delete Successfully");
				String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
				String encodedURL = "redirect:/orders/search?keyword=" + encodedKeyword + "&pageNo=" + pageNo;
				return encodedURL;
			}else {
				m.addAttribute("currentPage", orderList.getNumber());

				m.addAttribute("searchProduct", "search Product");
				m.addAttribute("success", "Delete Successfully");
				
				if(pageNo -1 >= 1) {
					pageNo = pageNo -1;
				}
				
				String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
				String encodedURL = "redirect:/orders/search?keyword=" + encodedKeyword + "&pageNo=" + pageNo;
				return encodedURL;
			}


		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("failed", "Delete Failed");
		}
		return "orders";
	}

//	@PostMapping(value = "/products/search/update")
//	public String updateSearchProduct(ProductDto productDto, @RequestParam("imageProduct") MultipartFile imageProduct,
//			Model m, String keyword, @RequestParam(name = "pageNo") int pageNo) {
//
//		Product product = this.productService.update(productDto, imageProduct);
//		Page<Product> productList = this.productService.searchProducts(keyword, pageNo);
//
//		try {
//			if (product != null) {
//				m.addAttribute("productList", productList);
//				m.addAttribute("keyword", keyword);
//
//				m.addAttribute("title", "PRODUCT");
//				m.addAttribute("totalPage", productList.getTotalPages());
//
////			trang bắt đầu là 0
//				m.addAttribute("currentPage", productList.getNumber() + 1);
//				m.addAttribute("categoryList", categoryService.findAllByActivated());
//
//				m.addAttribute("searchProduct", "search Product");
//
//				m.addAttribute("success", "Update Successfully");
//			} else {
//				m.addAttribute("failed", "Update Failed because Duplicate ");
//			}
//		} catch (Exception e) {
//			m.addAttribute("failed", "Update Failed");
//		}
//		return "products";
//	}
	
	@GetMapping("/orders/acceptById")
	public String acceptOrder(Long id, RedirectAttributes redirectAttributes, int currentPage) {
		try {
			this.orderService.acceptOrder(id);
			redirectAttributes.addFlashAttribute("success", "Accept Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("failed", "Accept Failed");
		}
		return "redirect:/orders/" + currentPage;
	}
	
	@GetMapping("/orders/search/acceptById")
	public String acceptOrderSearch(Long id, Model m, String keyword, @RequestParam(name = "pageNo") int pageNo) {
		try {
			this.orderService.acceptOrder(id);
			
			Page<Order> orderList = this.orderService.searchOrders(keyword, pageNo);
			
			int totalPage = orderList.getTotalPages();
			List<Order> orderListNotNull = new ArrayList<>();
			for(int i=1; i<= totalPage; i++) {
				Page<Order> temp = this.orderService.searchOrders(keyword, i);
				orderListNotNull.addAll(temp.getContent());
			}
			
			Pageable pageable = PageRequest.of(pageNo - 1, 3);
			final int start = (int)pageable.getOffset();
			final int end = Math.min((start + pageable.getPageSize()), orderListNotNull.size());
			Page<Order> orderListNotNullPage = new PageImpl<>(orderListNotNull.subList(start, end), pageable, orderListNotNull.size());
			
			
			m.addAttribute("orderList", orderListNotNullPage);
			m.addAttribute("keyword", keyword);

			m.addAttribute("title", "Order");
			m.addAttribute("totalPage", orderListNotNullPage.getTotalPages());

//	trang bắt đầu là 0
			m.addAttribute("currentPage", orderListNotNullPage.getNumber() + 1);

			m.addAttribute("searchProduct", "search Product");
			m.addAttribute("success", "Accept Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("failed", "Accept Failed");
		}
		return "orders";
	}

	
//	hủy đơn (không phải xóa đơn)
	@GetMapping("/orders/refuseById")
	public String refuse(Long id, RedirectAttributes redirectAttributes, int currentPage) {
		try {
			this.orderService.refuseById(id);
			redirectAttributes.addFlashAttribute("success", "Refuse Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("failed", "Accept Failed");
		}
		return "redirect:/orders/" + currentPage;
	}
	
	@GetMapping("/orders/search/refuseById")
	public String refuseSearch(Long id, Model m, String keyword, @RequestParam(name = "pageNo") int pageNo) {
		try {
			this.orderService.refuseById(id);
			
			Page<Order> orderList = this.orderService.searchOrders(keyword, pageNo);
			
			int totalPage = orderList.getTotalPages();
			List<Order> orderListNotNull = new ArrayList<>();
			for(int i=1; i<= totalPage; i++) {
				Page<Order> temp = this.orderService.searchOrders(keyword, i);
				orderListNotNull.addAll(temp.getContent());
			}
			
			Pageable pageable = PageRequest.of(pageNo - 1, 3);
			final int start = (int)pageable.getOffset();
			final int end = Math.min((start + pageable.getPageSize()), orderListNotNull.size());
			Page<Order> orderListNotNullPage = new PageImpl<>(orderListNotNull.subList(start, end), pageable, orderListNotNull.size());
			
			
			m.addAttribute("orderList", orderListNotNullPage);
			m.addAttribute("keyword", keyword);

			m.addAttribute("title", "Order");
			m.addAttribute("totalPage", orderListNotNullPage.getTotalPages());

//	trang bắt đầu là 0
			m.addAttribute("currentPage", orderListNotNullPage.getNumber() + 1);

			m.addAttribute("searchProduct", "search Product");
			m.addAttribute("success", "Refuse Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("failed", "Refuse Failed");
		}
		return "orders";
	}
	
	@GetMapping("/findAllOrder")
	@ResponseBody
	public List<Object[]> findAllToTalPriceOfOrderByMonth() {
		List<Object[]> orderList = this.orderService.findAllToTalPriceOfOrderByMonth();
		return orderList;
	}
}

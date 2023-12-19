package com.hokhanh.customer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hokhanh.libary.model.CartItem;
import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.model.Order;
import com.hokhanh.libary.model.Product;
import com.hokhanh.libary.model.ShoppingCart;
import com.hokhanh.libary.repository.CustomerRepository;
import com.hokhanh.libary.service.CustomerService;
import com.hokhanh.libary.service.ProductService;
import com.hokhanh.libary.service.ShoppingCartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	public void checkTotalItemOfCustomer(Authentication authentication, Model model) {
		if (authentication != null) {
			Customer customer = this.customerService.findByUsername(authentication.getName());
			ShoppingCart shoppingCart = customer.getShoppingCarts();
			if (shoppingCart != null) {
				model.addAttribute("totalItems", shoppingCart.getTotalItems());
			}
		}
	}

	@GetMapping("/cart")
	public String cart(Model m, Authentication authentication, HttpSession session) {
		m.addAttribute("title", "Cart");

		Customer customer = this.customerService.findByUsername(authentication.getName());
		ShoppingCart shoppingCart = customer.getShoppingCarts();

		if (shoppingCart != null && shoppingCart.getCartItems() != null
				&& shoppingCart.getCartItems().isEmpty() == false) {
			List<CartItem> cartItemsDelete = new ArrayList<>();
			for (CartItem cartItem : shoppingCart.getCartItems()) {
				if (cartItem.getProduct().getCurrentQuantity() <= 0) {
					cartItemsDelete.add(cartItem);
				}
			}
			
			for (CartItem cartItem : cartItemsDelete) {
				this.shoppingCartService.deleteItemInCart(cartItem.getProduct(), authentication.getName());
			}
		}
		
		shoppingCart = customer.getShoppingCarts();

		if (shoppingCart != null && shoppingCart.getCartItems() != null
				&& shoppingCart.getCartItems().isEmpty() == false) {
			List<CartItem> cartItemsUpdate = new ArrayList<>();

			for (CartItem cartItem : shoppingCart.getCartItems()) {
				if (cartItem.getQuantity() > cartItem.getProduct().getCurrentQuantity()) {
					cartItem.setQuantity(cartItem.getProduct().getCurrentQuantity());
					cartItemsUpdate.add(cartItem);
				}
			}

			for (CartItem cartItem : cartItemsUpdate) {
				this.shoppingCartService.updateItemInCart(cartItem.getProduct(), cartItem.getQuantity(),
						authentication.getName());
			}

		}

		m.addAttribute("shoppingCart", shoppingCart);

		List<Product> products = this.productService.getAllProduct();
		m.addAttribute("productFeed", products);

		checkTotalItemOfCustomer(authentication, m);
		return "cart";
	}

	@PostMapping("/cart/addItem")
	public String addItemToCart(@RequestParam Long id, @RequestParam int quantity, Authentication authentication,
			HttpServletRequest httpServletRequest) {
		Product product = productService.findById(id);

		this.shoppingCartService.addItemToCart(product, quantity, authentication.getName());

		return "redirect:" + httpServletRequest.getHeader("Referer");
	}

	@RequestMapping(value = "/cart/update", method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseBody
	public String updateItemInCart(int quantity, Long id, Authentication authentication) {

		Product product = this.productService.findById(id);

		ShoppingCart cart = this.shoppingCartService.updateItemInCart(product, quantity, authentication.getName());
		if (cart != null) {
			CartItem cartItem = null;
			for (CartItem cartI : cart.getCartItems()) {
				if (cartI.getProduct().getId() == id) {
					cartItem = cartI;
				}
			}
			return cartItem.getTotalPrice() + "";
		} else {
			System.out.println("null");
			return "0";
		}
	}

	@RequestMapping(value = "/cart/delete", method = { RequestMethod.GET, RequestMethod.DELETE })
	@ResponseBody
	public String deleteItemInCart(Long id, Authentication authentication) {
		Product product = this.productService.findById(id);

		if (product == null) {
			return "failed";
		} else {
			ShoppingCart cart = this.shoppingCartService.deleteItemInCart(product, authentication.getName());
			if (cart != null) {
				return "good delete";
			} else {
				return "bad";
			}
		}
	}

	@GetMapping("/checkTotalPirce")
	@ResponseBody
	public String checkTotalPirceOfCart(Authentication authentication) {
		if (authentication != null) {
			Customer customer = this.customerService.findByUsername(authentication.getName());
			if (customer.getShoppingCarts() != null) {
				return customer.getShoppingCarts().getTotalPrice() + "";
			}
		}

		return "0";
	}

	@GetMapping("/checkEmptyCartItems")
	@ResponseBody
	public String checkEmptyCartItems(Authentication authentication) {
		if (authentication != null) {
			Customer customer = this.customerService.findByUsername(authentication.getName());
			ShoppingCart shoppingCart = customer.getShoppingCarts();
			if (shoppingCart != null) {
				if (shoppingCart.getCartItems() != null && shoppingCart.getCartItems().isEmpty() == false) {
					return "not empty";
				} else {
					return "empty";
				}
			} else {
				return "empty";
			}
		}

		return "empty";
	}
	
	
}

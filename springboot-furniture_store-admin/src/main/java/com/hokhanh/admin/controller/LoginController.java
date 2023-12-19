package com.hokhanh.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hokhanh.libary.dto.AdminDto;
import com.hokhanh.libary.model.Admin;
import com.hokhanh.libary.repository.AdminRepository;
import com.hokhanh.libary.service.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LoginController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/index")
	public String index(Model model, Authentication authentication, HttpSession httpSession) {
		Admin admin = this.adminService.findByUsername(authentication.getName());
		httpSession.setAttribute("admin", admin);
		model.addAttribute("title", "HOME PAGE");
		
		
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model, Authentication authentication) {
		model.addAttribute("title", "LOGIN");
		
		if(authentication != null) {
			return "redirect:/index";
		}
		
		return "login";
	}
	
	
	@GetMapping("/register")
	public String register(Model m) {
		m.addAttribute("title", "REGISTER");
		AdminDto adminDto = (AdminDto) m.asMap().get("adminDto");
		if(adminDto != null) {
			m.addAttribute("adminDto", adminDto);
		}else {
			m.addAttribute("adminDto", new AdminDto());
		}
		return "register";
	}
	
	@PostMapping("/register-new")
	public String registerNew(@Valid AdminDto adminDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		try {
			if(bindingResult.hasErrors()) {
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.adminDto", bindingResult);
				redirectAttributes.addFlashAttribute("adminDto", adminDto);
				return "redirect:/register";
			}
			
			Admin admin = this.adminService.findByUsername(adminDto.getUsername());
			if(admin != null) {
				redirectAttributes.addFlashAttribute("emailError", "Email already exists !");
				redirectAttributes.addFlashAttribute("adminDto", adminDto);
				return "redirect:/register";
			}
			
			if(adminDto.getPassword().equals(adminDto.getRepeatPassword())) {
				adminDto.setPassword(bCryptPasswordEncoder.encode(adminDto.getPassword()));
				this.adminService.save(adminDto);
				redirectAttributes.addFlashAttribute("success", "Register Successfully !");
				return "redirect:/register";
			}else {
				redirectAttributes.addFlashAttribute("adminDto",adminDto);
				redirectAttributes.addFlashAttribute("passwordError","Check password and repeat password !");
				return "redirect:/register";
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("adminDto",adminDto);
			redirectAttributes.addFlashAttribute("error","register failed !");
			return "redirect:/register";
		}
	}
	
	@GetMapping("/forgot-password")
	public String forgotPassword(Model model) {
		model.addAttribute("title", "FORGOT PASSWORD");
		return "forgot-password";
	}
}

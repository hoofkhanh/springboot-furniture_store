package com.hokhanh.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hokhanh.libary.model.Admin;
import com.hokhanh.libary.model.City;
import com.hokhanh.libary.model.Country;
import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.service.AdminService;
import com.hokhanh.libary.service.CityService;
import com.hokhanh.libary.service.CountryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileAdmin {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private CityService cityService;


	@GetMapping("profile")
	public String profile(Authentication authentication, Model m, HttpSession httpSession) {
		m.addAttribute("title", "Profile");
		
		Admin admin = this.adminService.findByUsername(authentication.getName());
		List<Country > countryList = this.countryService.findAll();
		List<City> cityList = this.cityService.findAll();
		
		Admin temp =  (Admin) m.asMap().get("admin");
		if(temp == null) {
			m.addAttribute("admin", admin);	
			httpSession.setAttribute("admin", admin);
		}else {
			m.addAttribute("admin", temp);
			httpSession.setAttribute("admin", temp);
		}
		
		
		m.addAttribute("admin", admin);
		m.addAttribute("countryList", countryList);
		m.addAttribute("cityList", cityList);
		return "profile";
	}
	
	@GetMapping("/account/getCityById")
	@ResponseBody
	public List<City> getCityById(Long id) {
		List<City> cityList = this.cityService.findByIdOfCountry(id);
		
		return cityList;
	}
	
	@PostMapping("/updateAdmin")
	public String updateAdmin(Admin admin ,RedirectAttributes redirectAttributes, MultipartFile adminImage) {
		Admin temp = this.adminService.update(admin, adminImage);
		
		if(temp != null && temp.getPhoneNumber() == null) {
			redirectAttributes.addFlashAttribute("phoneNumberError", "Số điện thoại của bạn đã tồn tại");
			redirectAttributes.addFlashAttribute("admin", admin);
		}else {
			redirectAttributes.addFlashAttribute("success", "Bạn đã chỉnh sửa thành công");
			redirectAttributes.addFlashAttribute("admin", admin);
		}				
		return "redirect:/profile";
	}
}

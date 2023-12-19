package com.hokhanh.customer.controller;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hokhanh.libary.dto.CustomerDto;
import com.hokhanh.libary.model.Customer;
import com.hokhanh.libary.service.CustomerService;
import com.hokhanh.libary.utils.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import net.bytebuddy.utility.RandomString;

@Controller
public class AuthController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private JavaMailSender mailSender;

	@GetMapping("/login")
	public String login(Model m, Authentication authentication, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		m.addAttribute("title", "LOGIN");
		
		if(authentication != null) {
			return "redirect:/home";
		}else {
			String username = (String) session.getAttribute("username");
			if(username != null) {
				session.setAttribute("username", null);
			}
		}
		
		String text = (String) m.asMap().get("success");
		if(text != null) {
			m.addAttribute("success", text);
		}
		
		CustomerDto customerDto = (CustomerDto) m.asMap().get("customer");
		if(customerDto != null) {
			m.addAttribute("customer", customerDto);
		}
		else {
			m.addAttribute("customer", new CustomerDto());
		}
		return "login";
	}

	@GetMapping("/register")
	public String register(Model m) {
		m.addAttribute("title", "REGISTER");

		CustomerDto customerDto = (CustomerDto) m.asMap().get("customerDto");
		if (customerDto != null) {
			m.addAttribute("customerDto", customerDto);
		} else {
			m.addAttribute("customerDto", new CustomerDto());
		}
		return "register";
	}

	@PostMapping("/register-new")
	public String registerNew(@Valid CustomerDto customerDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		try {
			if (bindingResult.hasErrors()) {
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.customerDto",
						bindingResult);
				redirectAttributes.addFlashAttribute("customerDto", customerDto);
				return "redirect:/register";
			}

			if (this.customerService.findByUsername(customerDto.getUsername()) != null) {
				redirectAttributes.addFlashAttribute("customerDto", customerDto);
				redirectAttributes.addFlashAttribute("failed", "Username already exists!");
				return "redirect:/register";
			}

			if (customerDto.getPassword().equals(customerDto.getRepeatPassword()) == false) {
				redirectAttributes.addFlashAttribute("customerDto", customerDto);
				redirectAttributes.addFlashAttribute("failed", "Check password and repeat password again!");
			} else {
				redirectAttributes.addFlashAttribute("success", "Sign up Successfully");
				redirectAttributes.addFlashAttribute("customer", customerDto);
				this.customerService.save(customerDto);
				return "redirect:/login";
			}

			return "redirect:/register";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("customerDto", customerDto);
			redirectAttributes.addFlashAttribute("failed", "Failed Server!");
			return "redirect:/register";
		}
	}

	@GetMapping("/forgot-password")
	public String forgotPassword(Model m) {
		m.addAttribute("title", "Forgot Password");
		return "forgot-password";
	}

	@PostMapping("/forgot-password")
	public String processForgotPassword(HttpServletRequest request, String email, Model model) {
		String token = RandomString.make(30);

		try {
			this.customerService.updateResetPasswordToken(token, email);
			String resetPasswordLink = Utility.getSiteUrl(request) + "/reset_password?token=" + token;
			sendEmail(email, resetPasswordLink);
			model.addAttribute("message", "Chúng tôi đã gửi link qua mail.");
		} catch (UsernameNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Error while sending email");
		}

		return "forgot-password";
	}
 
	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
		Customer customer = this.customerService.getByResetPasswordToken(token);
		model.addAttribute("token", token);
		model.addAttribute("title", "Reset Password");

		if (customer == null) {
			model.addAttribute("message", "Bạn đã thay đổi mật khẩu trước đó rồi");
			return "forgot-password";
		}
		
		return "reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model) {
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	     
	    Customer customer = customerService.getByResetPasswordToken(token);
	    model.addAttribute("title", "Reset your password");
	     
	    if (customer == null) {
	    	model.addAttribute("message", "Bạn đã thay đổi mật khẩu trước đó rồi");
			return "forgot-password";
	    } else {           
	        customerService.upadtePassword(customer, password);
	         
	        model.addAttribute("message", "Bạn đã thay đổi mật khẩu thành công.");
	    }
	     
	    return "forgot-password";
	}

	public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("hotuankhanh20112016@gmail.com", "HoKhanh Support");
		helper.setTo(recipientEmail);

		String subject = "Đây là liên kết giúp bân khôi phục mật khẩu";

		String content = "<p>Hello,</p>" + "<p>Bạn có một yêu cầu để khôi phục password.</p>"
				+ "<p>Bấm vào link để thay đổi password:</p>" + "<p><a href=\"" + link + "\">Thay đổi mật khẩu</a></p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}
	
	@GetMapping("/change-password")
	public String changePassword(Model m) {
		m.addAttribute("title", "Thay đổi mật khẩu");
		return "change-password";
	}
	
	@PostMapping("/changePassword")
	public String processChangePassword(String username,String password,String newPassword,Model m) {
		System.out.println(password);
		Customer customer = this.customerService.findByUsername(username);
		Customer customer_2 = this.customerService.findByPassword(password, username);
		if(customer == null) {
			m.addAttribute("failed", "Không tìm thấy email này");
			m.addAttribute("username", username);
			m.addAttribute("password", password);
			m.addAttribute("newPassword", newPassword);
			return "change-password";
		}
		
		if(customer_2 == null) {
			m.addAttribute("failed", "Mật khẩu sai");
			m.addAttribute("username", username);
			m.addAttribute("password", password);
			m.addAttribute("newPassword", newPassword);
			return "change-password";
		}
		
		this.customerService.upadtePassword(customer, newPassword);
		m.addAttribute("success", "Thay đổi mật khẩu thành công");
		return "change-password";
	}
}

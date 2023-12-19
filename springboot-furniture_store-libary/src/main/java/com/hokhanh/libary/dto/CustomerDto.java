package com.hokhanh.libary.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
	
	@Size(min = 2, max = 20, message = "Tên riêng nên có 2 đến 20 kí tự")
	private String firstName;
	
	@Size(min = 2, max = 20, message = "Họ nên có 2 đến 20 kí tự")
	private String lastName;
	
	private String username;
	
	@Size(min = 2, max = 20, message = "Mật khẩu nên có 2 đến 20 kí tự")
	private String password;
	
	private String repeatPassword;
}

package com.hokhanh.admin.configSecurity;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class AdminConfiguration {
	
	@Autowired
	private AdminServiceSecurity adminService;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
		.authorizeHttpRequests(request -> request	// login , register ,forgot-password khỏi cần ghi vì tự động vô được
			.requestMatchers("/resources/**", "/css/**", "/img/**", "/vendor/**", "/js/**", "/scss/*").permitAll()
			.requestMatchers("/login").permitAll()
			.requestMatchers("/register", "/register-new").permitAll()
			.requestMatchers("/forgot-password").permitAll()
		    .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
			.anyRequest().authenticated()
		)
		.formLogin(form -> form
			.loginPage("/login") 
			.loginProcessingUrl("/do-login")
			.defaultSuccessUrl("/index", true)
			.permitAll()
		)
		.rememberMe(remember -> remember.tokenRepository(persistenTokenRepository()))
		.logout(logout -> logout
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login?logout")
			.permitAll()
		)
		
		;
		
		return http.build();
	}
	
	@Bean
	public PersistentTokenRepository persistenTokenRepository() {
		JdbcTokenRepositoryImpl impl =new JdbcTokenRepositoryImpl();
		impl.setDataSource(this.dataSource);
		return impl;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(this.adminService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

}

package com.hokhanh.libary.model;

import java.time.LocalDate;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long id;
	
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String phoneNumber;
	private String address;
	private String nameOfBank;
	private LocalDate birthDay;
	private String gender;
	
	@Column(name = "reset_password_token")
	private String resetPasswordToken;
	
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String image;
	
	@Column(unique = true)
	private String accountNumber;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "city_id", referencedColumnName = "city_id")
	private City city;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "customers_roles", joinColumns = @JoinColumn(name="customer_id", referencedColumnName = "customer_id")
				, inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "role_id"))
	private List<Role> roleList;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
	private ShoppingCart shoppingCarts ;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	private List<Order> orders;
}

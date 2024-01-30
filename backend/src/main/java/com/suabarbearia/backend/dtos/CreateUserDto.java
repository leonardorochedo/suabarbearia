package com.suabarbearia.backend.dtos;

import com.suabarbearia.backend.entities.Address;

public class CreateUserDto {

	private String name;
	private String email;
	private String cpf;
	private String birth;
	private String phone;
	private Address address;
	private String password;
	private String confirmpassword;

	
	public CreateUserDto() {}

	public CreateUserDto(String name, String email, String cpf, String birth, String phone, Address address, String password, String confirmpassword) {
		this.name = name;
		this.email = email;
		this.cpf = cpf;
		this.birth = birth;
		this.phone = phone;
		this.address = address;
		this.password = password;
		this.confirmpassword = confirmpassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmpassword() {
		return confirmpassword;
	}

	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}

}

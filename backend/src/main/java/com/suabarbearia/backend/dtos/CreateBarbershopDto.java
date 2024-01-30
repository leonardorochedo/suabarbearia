package com.suabarbearia.backend.dtos;

import com.suabarbearia.backend.entities.Address;

import java.time.LocalTime;

public class CreateBarbershopDto {

	private String name;
	private String email;
	private String document;
	private String birth;
	private String phone;
	private Address address;
	private LocalTime openTime;
	private LocalTime closeTime;
	private String password;
	private String confirmpassword;
	
	public CreateBarbershopDto() {}

	public CreateBarbershopDto(String name, String email, String document, String birth, String phone, Address address, LocalTime openTime, LocalTime closeTime, String password, String confirmpassword) {
		this.name = name;
		this.email = email;
		this.document = document;
		this.birth = birth;
		this.phone = phone;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
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

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
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

	public LocalTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	public LocalTime getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
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

package com.suabarbearia.backend.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.time.LocalTime;

public class EditBarbershopDto {

    private MultipartFile image;
    private String name;
    private String email;
    private String password;
    private String confirmpassword;
    private String phone;
    private String address;
    private String cep;
    private LocalTime openTime;
    private LocalTime closeTime;

    public EditBarbershopDto() {}

    public EditBarbershopDto(MultipartFile image, String name, String email, String password, String confirmpassword, String phone, String address, String cep, LocalTime openTime, LocalTime closeTime) {
        super();
        this.image = image;
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.phone = phone;
        this.address = address;
        this.cep = cep;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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

}

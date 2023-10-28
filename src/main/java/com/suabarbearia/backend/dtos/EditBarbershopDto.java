package com.suabarbearia.backend.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

public class EditBarbershopDto {

    private MultipartFile image;
    private String name;
    private String email;
    private String password;
    private String confirmpassword;
    private String phone;
    private String address;

    public EditBarbershopDto() {}

    public EditBarbershopDto(MultipartFile image, String name, String email, String password, String confirmpassword, String phone, String address) {
        super();
        this.image = image;
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.phone = phone;
        this.address = address;
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

}

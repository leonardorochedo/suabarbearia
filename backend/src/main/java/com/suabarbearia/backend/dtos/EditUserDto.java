package com.suabarbearia.backend.dtos;

import org.springframework.web.multipart.MultipartFile;

public class EditUserDto {

    private String name;
    private String email;
    private String password;
    private String confirmpassword;
    private String phone;
    private String address;
    private String cep;
    private MultipartFile image;

    public EditUserDto() {}

    public EditUserDto(String name, String email, String password, String confirmpassword, String phone, String address, String cep, MultipartFile image) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.phone = phone;
        this.address = address;
        this.cep = cep;
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

}

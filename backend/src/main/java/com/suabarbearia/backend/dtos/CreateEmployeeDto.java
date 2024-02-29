package com.suabarbearia.backend.dtos;

public class CreateEmployeeDto {

    private String name;
    private String username;
    private String password;
    private String confirmpassword;
    private String phone;
    private Double commission;

    public CreateEmployeeDto () {}

    public CreateEmployeeDto(String name, String username, String password, String confirmpassword, String phone, Double commission) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.phone = phone;
        this.commission = commission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }
}

package com.suabarbearia.backend.dtos;

public class ChangePasswordDto {

    private String password;
    private String confirmpassword;

    public ChangePasswordDto() {}

    public ChangePasswordDto(String password, String confirmpassword) {
        this.password = password;
        this.confirmpassword = confirmpassword;
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

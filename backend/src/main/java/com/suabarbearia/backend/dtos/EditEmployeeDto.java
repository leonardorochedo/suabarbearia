package com.suabarbearia.backend.dtos;

public class EditEmployeeDto {
   private String username;
   private String name;
   private String password;
   private String confirmpassword;
   private String phone;
   private byte[] image;

   public EditEmployeeDto() {}

   public EditEmployeeDto(String currentUsername, String username, String password, String confirmpassword, String phone, byte[] image) {
      this.username = username;
      this.password = password;
      this.confirmpassword = confirmpassword;
      this.phone = phone;
      this.image = image;
   }

   public String getConfirmpassword() {
      return confirmpassword;
   }

   public void setConfirmpassword(String confirmpassword) {
      this.confirmpassword = confirmpassword;
   }

   public String getUsername() {
      return username;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
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

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public byte[] getImage() {
      return image;
   }

   public void setImage(byte[] image) {
      this.image = image;
   }
}

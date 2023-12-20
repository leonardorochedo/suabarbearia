package com.suabarbearia.backend.dtos;

public class EditEmployeeDto {
   private String newUsername;
   private String name;
   private String password;
   private String phone;
   private byte[] image;

   public EditEmployeeDto() {}

   public EditEmployeeDto(String currentUsername, String username, String password, String phone, byte[] image) {
      this.newUsername = username;
      this.password = password;
      this.phone = phone;
      this.image = image;
   }

   public String getUsername() {
      return newUsername;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUsername(String username) {
      this.newUsername = username;
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

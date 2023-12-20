package com.suabarbearia.backend.dtos;

public class SigninEmployeeDto {

   private String username;
   private String password;

   public SigninEmployeeDto(String username, String password) {
      super();
      this.username = username;
      this.password = password;
   }
   
   public String getUsername() {
      return username;
   }
   public void setUsername(String email) {
      this.username = email;
   }
   public String getPassword() {
      return password;
   }
   public void setPassword(String password) {
      this.password = password;
   }
   
}

package com.suabarbearia.backend.dtos;

public class SigninDto {
   
   private String email;
   private String password;
   
   public SigninDto(String email, String password) {
      super();
      this.email = email;
      this.password = password;
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
   
}

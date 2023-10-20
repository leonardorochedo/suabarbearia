package com.suabarbearia.backend.responses;

public class ApiTokenResponse<T> {
	
	private String message;
	private String token;
	private T data;
		
	public ApiTokenResponse(String message, String token, T data) {
		super();
		this.message = message;
		this.token = token;
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
}

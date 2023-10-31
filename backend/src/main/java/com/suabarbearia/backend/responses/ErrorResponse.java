package com.suabarbearia.backend.responses;

public class ErrorResponse {

	private String message;
	
	public ErrorResponse(String message) {
		super();
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}

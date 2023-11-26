package com.suabarbearia.backend.exceptions;

public class InvalidTokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String text) {
		super(text);
	}

}

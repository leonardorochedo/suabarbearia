package com.suabarbearia.backend.exceptions;

public class PasswordDontMatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PasswordDontMatchException(String text) {
		super(text);
	}

}

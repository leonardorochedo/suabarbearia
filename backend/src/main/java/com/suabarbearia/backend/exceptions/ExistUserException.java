package com.suabarbearia.backend.exceptions;

public class ExistUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistUserException(String text) {
		super(text);
	}

}

package com.suabarbearia.backend.exceptions;

public class NoTokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoTokenException(String text) {
		super(text);
	}

}

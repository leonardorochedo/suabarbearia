package com.suabarbearia.backend.exceptions;

public class InvalidDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidDataException(String text) {
		super(text);
	}

}

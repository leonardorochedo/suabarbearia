package com.suabarbearia.backend.exceptions;

public class FieldsAreNullException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FieldsAreNullException(String text) {
		super(text);
	}

}

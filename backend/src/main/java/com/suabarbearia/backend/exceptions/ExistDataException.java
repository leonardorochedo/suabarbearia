package com.suabarbearia.backend.exceptions;

public class ExistDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistDataException(String text) {
		super(text);
	}

}

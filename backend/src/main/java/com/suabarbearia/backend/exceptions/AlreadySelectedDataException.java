package com.suabarbearia.backend.exceptions;

public class AlreadySelectedDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadySelectedDataException(String text) {
		super(text);
	}

}

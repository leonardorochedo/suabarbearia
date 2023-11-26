package com.suabarbearia.backend.exceptions;

public class DisabledDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DisabledDataException(String text) {
		super(text);
	}

}

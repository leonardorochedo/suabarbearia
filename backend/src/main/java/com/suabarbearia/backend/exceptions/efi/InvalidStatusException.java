package com.suabarbearia.backend.exceptions.efi;

public class InvalidStatusException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidStatusException(String text) {
		super(text);
	}

}

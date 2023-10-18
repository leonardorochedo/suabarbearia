package com.suabarbearia.backend.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String text) {
		super(text);
	}

}

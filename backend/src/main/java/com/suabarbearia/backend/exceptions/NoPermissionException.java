package com.suabarbearia.backend.exceptions;

public class NoPermissionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoPermissionException(String text) {
		super(text);
	}

}

package com.suabarbearia.backend.exceptions.efi;

public class InsufficientMoneyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientMoneyException(String text) {
		super(text);
	}

}

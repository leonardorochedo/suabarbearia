package com.suabarbearia.backend.exceptions;

public class LastSchedulingNotDoneException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LastSchedulingNotDoneException(String text) {
		super(text);
	}

}

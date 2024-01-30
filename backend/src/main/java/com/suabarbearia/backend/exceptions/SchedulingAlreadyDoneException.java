package com.suabarbearia.backend.exceptions;

public class SchedulingAlreadyDoneException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SchedulingAlreadyDoneException(String text) {
		super(text);
	}

}

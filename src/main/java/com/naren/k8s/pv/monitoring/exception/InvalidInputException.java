package com.naren.k8s.pv.monitoring.exception;

public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidInputException(String field, Object value) {
		super("Invalid input for :" + field + "( " + value + ")");
	}

}

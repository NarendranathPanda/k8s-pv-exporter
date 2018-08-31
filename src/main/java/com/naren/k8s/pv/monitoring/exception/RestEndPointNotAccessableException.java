package com.naren.k8s.pv.monitoring.exception;

public class RestEndPointNotAccessableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RestEndPointNotAccessableException(String url, Throwable e) {
		super("Error in accessing the url: " + url, e);
	}
}

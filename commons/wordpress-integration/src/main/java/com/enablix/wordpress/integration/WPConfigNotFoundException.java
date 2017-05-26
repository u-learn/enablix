package com.enablix.wordpress.integration;

public class WPConfigNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public WPConfigNotFoundException() {
	}

	public WPConfigNotFoundException(String message) {
		super(message);
	}
	
	public WPConfigNotFoundException(String message, Throwable t) {
		super(message, t);
	}
}

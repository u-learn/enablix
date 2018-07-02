package com.enablix.ms.graph;

public class MSGraphException extends Exception {

	private static final long serialVersionUID = 1L;

	public MSGraphException(String message) {
		super(message);
	}
	
	public MSGraphException(String message, Throwable t) {
		super(message, t);
	}
	
}

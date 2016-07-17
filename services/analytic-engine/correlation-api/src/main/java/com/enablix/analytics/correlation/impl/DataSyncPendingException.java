package com.enablix.analytics.correlation.impl;

public class DataSyncPendingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataSyncPendingException() {
		super();
	}
	
	public DataSyncPendingException(String message) {
		super(message);
	}
	
}

package com.enablix.dms.sharepoint;

public class SharepointException extends Exception {

	private static final long serialVersionUID = 1L;

	public SharepointException(String message) {
		super(message);
	}
	
	public SharepointException(String message, Throwable t) {
		super(message, t);
	}
	
}

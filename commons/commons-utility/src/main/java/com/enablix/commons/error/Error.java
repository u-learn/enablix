package com.enablix.commons.error;

public class Error {

	private String errorCode;
	
	private String errorMessage;
	
	public Error(String errCode, String errMessage) {
		this.errorCode = errCode;
		this.errorMessage = errMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}

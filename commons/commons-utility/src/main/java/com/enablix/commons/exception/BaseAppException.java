package com.enablix.commons.exception;

public class BaseAppException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private AppError error;
	private int httpErrorCode;
	
	public BaseAppException(AppError error, Throwable t) {
		super(error.getErrorMessage(), t);
		this.error = error;
	}
	
	public BaseAppException(AppError error) {
		super(error.getErrorMessage());
		this.error = error;
	}
	
	public BaseAppException(AppError error, int httpErrorCode) {
		this(error);
		this.httpErrorCode = httpErrorCode;
	}

	public AppError getError() {
		return error;
	}

	public void setError(AppError error) {
		this.error = error;
	}

	public int getHttpErrorCode() {
		return httpErrorCode;
	}

	public void setHttpErrorCode(int httpErrorCode) {
		this.httpErrorCode = httpErrorCode;
	}
	
}

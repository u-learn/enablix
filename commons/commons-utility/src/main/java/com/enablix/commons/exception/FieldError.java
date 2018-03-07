package com.enablix.commons.exception;

public class FieldError extends AppError {

	private String field;
	
	public FieldError(String errorCode, String errorMessage, String fieldName) {
		super(errorCode, errorMessage);
		this.field = fieldName;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	

}

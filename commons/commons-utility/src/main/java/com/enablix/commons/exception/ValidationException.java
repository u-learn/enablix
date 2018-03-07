package com.enablix.commons.exception;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.http.HttpStatus;

public class ValidationException extends ErrorCollection {

	private static final long serialVersionUID = 1L;

	public ValidationException() {
		this(new ArrayList<>());
	}
	
	public ValidationException(Collection<AppError> errors) {
		super(new AppError(ErrorCodes.VALIDATION_ERROR, "Validation error"), HttpStatus.BAD_REQUEST.value());
		super.errors.addAll(errors);
	}

}

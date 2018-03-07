package com.enablix.commons.exception;

import java.util.ArrayList;
import java.util.Collection;

public class ErrorCollection extends BaseAppException {

	private static final long serialVersionUID = 1L;

	protected Collection<AppError> errors;

	public ErrorCollection(AppError error, Throwable t) {
		super(error, t);
		this.errors = new ArrayList<>();
	}
	
	public ErrorCollection(AppError error) {
		super(error);
		this.errors = new ArrayList<>();
	}
	
	public ErrorCollection(AppError error, int httpErrorCode) {
		super(error, httpErrorCode);
		this.errors = new ArrayList<>();
	}

	public Collection<AppError> getErrors() {
		return errors;
	}
	
	public void addError(AppError error) {
		this.errors.add(error);
	}

}

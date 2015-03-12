package com.enablix.app.service;

import java.util.Collection;

public class CrudResponse<T> {

	private Collection<Error> errors;
	
	private T payload;

	public Collection<Error> getErrors() {
		return errors;
	}
	
	public CrudResponse() {
		this(null);
	}
	
	public CrudResponse(T payload) {
		this.payload = payload;
	}

	public void setErrors(Collection<Error> errors) {
		this.errors = errors;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}
	
}

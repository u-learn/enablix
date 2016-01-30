package com.enablix.app.service;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.core.domain.BaseEntity;

public abstract class AbstractCrudService<T extends BaseEntity> implements CrudService<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCrudService.class);
	
	@Override
	public CrudResponse<T> saveOrUpdate(T t) {
		
		LOGGER.debug("save or update entity executing");
		
		CrudResponse<T> response = new CrudResponse<>();
		
		Collection<Error> errors = validate(t);
		
		if (errors != null && !errors.isEmpty()) {
			
			response.setErrors(errors);
			
		} else {
			
			T existing = findExisting(t);
			if (existing != null) {
				t = merge(t, existing);
			}
			
			preSaveOperation(t);
			T saved = doSave(t);
			postSaveOperation(saved);
			
			response.setPayload(saved);
		}
		
		return response;
	}
	
	protected void preSaveOperation(T t) { }
	
	protected void postSaveOperation(T t) { } 

	public Collection<Error> validate(T t) {
		// default implementation does no validation
		return new ArrayList<Error>();
	};
	
	protected abstract T doSave(T t);
	
	protected abstract T merge(T t, T existing);

	protected abstract T findExisting(T t);
	
}

package com.enablix.commons.validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.enablix.commons.exception.AppError;

@SuppressWarnings("rawtypes")
public class Validators {
	
	
	private Collection<Validator> validators;

	private Validators(Collection<Validator> validators) {
		this.validators = validators;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Collection<AppError> validate(T input) {
		
		Collection<AppError> errors = new ArrayList<>();
		
		validators.forEach((validator) -> {
			AppError error = validator.validate(input);
			if (error != null) {
				errors.add(error);
			}
 		});
		
		return errors;
	}
	
	public static Validators list(Validator... validators) {
		return new Validators(Arrays.asList(validators));
	}

}

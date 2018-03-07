package com.enablix.commons.validate;

import com.enablix.commons.exception.AppError;

public interface Validator<T> {

	 AppError validate(T input);
	
}

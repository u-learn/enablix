package com.enablix.commons.validate;

import java.util.Collection;

public interface Validator<T> {

	Collection<Error> validate(T t);
	
}

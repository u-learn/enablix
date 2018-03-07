package com.enablix.commons.validate;

import com.enablix.commons.exception.ErrorCodes;
import com.enablix.commons.exception.FieldError;
import com.enablix.commons.util.StringUtil;

public class ValueExistValidator<T> extends PropertyValidator implements Validator <T> {

	private ValueExistValidator(String propName) {
		super(propName);
	}
	
	@Override
	public FieldError validate(T input) {
		
		Object value = getPropertyValue(input);
		
		if (value == null || StringUtil.isStringAndEmpty(value)) {
			return new FieldError(ErrorCodes.VALUE_MISSING, "Value not found for [" + propName + "]", propName);
		}
		
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static ValueExistValidator forProp(String propName) {
		return new ValueExistValidator(propName);
	}

}

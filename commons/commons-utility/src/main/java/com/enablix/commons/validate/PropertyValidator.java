package com.enablix.commons.validate;

import org.apache.commons.beanutils.PropertyUtils;

public abstract class PropertyValidator {
	
	protected String propName;
	
	protected PropertyValidator(String propName) {
		this.propName = propName;
	}

	protected Object getPropertyValue(Object input) {
		
		Object value = null;
		
		try {
			value = PropertyUtils.getProperty(input, propName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return value;
	}
	
}

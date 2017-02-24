package com.enablix.commons.util.beans;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.enablix.commons.constants.ContentDataConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BeanUtil {

	private static final String[] NON_BUSINESS_PROPERTIES = {
			ContentDataConstants.ID_FLD_KEY, 
			ContentDataConstants.IDENTITY_KEY, 
			ContentDataConstants.CREATED_AT_KEY,
			ContentDataConstants.CREATED_BY_KEY,
			ContentDataConstants.CREATED_BY_NAME_KEY,
			ContentDataConstants.MODIFIED_AT_KEY,
			ContentDataConstants.MODIFIED_BY_KEY,
			ContentDataConstants.MODIFIED_BY_NAME_KEY};
	
	public static void copyBusinessAttributes(Object source, Object target) {
		BeanUtils.copyProperties(source, target, NON_BUSINESS_PROPERTIES);
	}

	public static Map<?,?> beanToMap(Object bean) {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(bean, HashMap.class);
	}
	
	public static <T> T mapToBean(Map<?, ?> map, Class<T> beanType) {
		
		T newInstance = null;
		
		try {
			newInstance = beanType.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			new RuntimeException("Unable to create instance of type [" + beanType.getName() + "]");
		} 
		
		BeanUtils.copyProperties(map, newInstance, beanType);
		return newInstance;
	}
		
}

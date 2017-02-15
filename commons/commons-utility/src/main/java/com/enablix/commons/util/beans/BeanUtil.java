package com.enablix.commons.util.beans;

import org.springframework.beans.BeanUtils;

import com.enablix.commons.constants.ContentDataConstants;

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
		
}

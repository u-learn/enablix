package com.enablix.services.util;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.ContainerType;

public class DatastoreUtil {

	public static final String COLL_NAME_SEP = "_";
	
	public static String getCollectionName(String templateId, ContainerType container) {
		return getCollectionName(templateId, container.getQualifiedId());
	}
	
	public static String getCollectionName(String templateId, String qualifiedId) {
		String[] split = qualifiedId.split("\\" + ContentDataConstants.QUALIFIED_ID_SEP);
		return templateId + COLL_NAME_SEP + split[split.length - 1];
	}
	
}

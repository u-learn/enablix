package com.enablix.services.util;

import com.enablix.commons.constants.AppConstants;

public class ElasticSearchUtil {

	public static final String getIndexName() {
		return DatastoreUtil.getTenantAwareDbName(AppConstants.BASE_DB_NAME);
	}
	
}

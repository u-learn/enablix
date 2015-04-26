package com.enablix.commons.util.tenant;

import com.enablix.commons.util.process.ProcessContext;

public class TenantUtil {

	public static String getTenantId() {
		return ProcessContext.get().getTenantId();
	}
	
}

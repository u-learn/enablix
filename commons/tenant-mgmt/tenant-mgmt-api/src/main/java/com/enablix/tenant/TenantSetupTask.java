package com.enablix.tenant;

import com.enablix.commons.util.ExecutionOrderAware;
import com.enablix.core.domain.tenant.Tenant;

public interface TenantSetupTask extends ExecutionOrderAware {

	void execute(Tenant tenant) throws Exception;
	
	int MIN_EXEC_ORDER = 0;
	int MAX_EXEC_ORDER = 1000;
}

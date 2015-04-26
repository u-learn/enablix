package com.enablix.core.system.repo;

import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface TenantRepository extends BaseMongoRepository<Tenant> {

	Tenant findByTenantId(String tenantId);
	
}

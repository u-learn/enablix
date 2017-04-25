package com.enablix.core.security.auth.repo;

import com.enablix.core.domain.tenant.TenantClient;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ClientRepository extends BaseMongoRepository<TenantClient> {

	TenantClient findByClientId(String clientId);
	
}

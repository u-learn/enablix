package com.enablix.core.security.hubspot.repo;

import com.enablix.core.mongo.repository.BaseMongoRepository;
import com.enablix.core.security.hubspot.HubspotAuthToken;

public interface HubspotAuthTokenRepository extends BaseMongoRepository<HubspotAuthToken> {
	
	HubspotAuthToken findByTenantId(String tenantId);
	
}

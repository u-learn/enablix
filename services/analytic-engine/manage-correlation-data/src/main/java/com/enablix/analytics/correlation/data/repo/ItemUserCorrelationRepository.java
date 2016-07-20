package com.enablix.analytics.correlation.data.repo;

import com.enablix.core.correlation.ItemUserCorrelation;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ItemUserCorrelationRepository extends BaseMongoRepository<ItemUserCorrelation> {

	ItemUserCorrelation findByItemInstanceIdentityAndUserInstanceIdentity(String itemIdentity, String userIdentity);

	void deleteByItemInstanceIdentity(String itemIdentity);
	
}

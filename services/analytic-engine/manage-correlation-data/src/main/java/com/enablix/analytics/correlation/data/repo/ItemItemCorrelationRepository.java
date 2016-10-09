package com.enablix.analytics.correlation.data.repo;

import com.enablix.core.correlation.ItemItemCorrelation;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ItemItemCorrelationRepository extends BaseMongoRepository<ItemItemCorrelation> {

	ItemItemCorrelation findByItemInstanceIdentityAndRelatedItemInstanceIdentity(
			String itemIdentity, String relatedItemIdentity);
	
	void deleteByItemInstanceIdentity(String itemIdentity);
	
	void deleteByItemInstanceIdentityAndItemCorrelationRuleId(String itemIdentity, String ruleId);
	
}

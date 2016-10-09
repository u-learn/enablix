package com.enablix.analytics.correlation.data.repo;

import org.springframework.data.mongodb.repository.Query;

import com.enablix.core.correlation.ItemUserCorrelation;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ItemUserCorrelationRepository extends BaseMongoRepository<ItemUserCorrelation> {

	ItemUserCorrelation findByItemInstanceIdentityAndUserInstanceIdentity(String itemIdentity, String userIdentity);

	void deleteByItemInstanceIdentity(String itemIdentity);
	
	@Query( value = "{ $and : [ {'item.instanceIdentity' : ?0}, {'sources.metadata.correlationRuleId' : ?1} ] }", delete = true)
	void deleteByItemInstanceIdentityAndCorrRule(String itemIdentity, String corrRuleId);
	
}


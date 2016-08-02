package com.enablix.core.system.repo;

import com.enablix.core.domain.share.SharedSiteUrl;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface SharedSiteUrlRepository extends BaseMongoRepository<SharedSiteUrl> {

	SharedSiteUrl findBySharedUrl(String sharedUrl);
	
}

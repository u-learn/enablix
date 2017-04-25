package com.enablix.app.content.ui.link.repo;

import java.util.List;

import com.enablix.core.domain.links.QuickLinkCategory;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface QuickLinkCategoryRepository extends BaseMongoRepository<QuickLinkCategory> {

	QuickLinkCategory findByName(String name);
	
	List<QuickLinkCategory> findByClientIdNull();
	
	List<QuickLinkCategory> findByClientId(String clientId);
	
}

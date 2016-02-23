package com.enablix.app.content.ui.link.repo;

import com.enablix.core.domain.links.QuickLinkCategory;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface QuickLinkCategoryRepository extends BaseMongoRepository<QuickLinkCategory> {

	QuickLinkCategory findByName(String name);
	
}

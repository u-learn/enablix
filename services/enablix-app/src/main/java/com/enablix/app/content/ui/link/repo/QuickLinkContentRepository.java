package com.enablix.app.content.ui.link.repo;

import java.util.List;

import com.enablix.core.domain.links.QuickLinkContent;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface QuickLinkContentRepository extends BaseMongoRepository<QuickLinkContent> {

	List<QuickLinkContent> findByDataInstanceIdentity(String identity);
	
	Long deleteByCategoryId(String categoryId);
	
}

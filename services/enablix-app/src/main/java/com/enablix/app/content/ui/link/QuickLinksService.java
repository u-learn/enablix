package com.enablix.app.content.ui.link;

import java.util.List;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.links.QuickLinkCategory;

public interface QuickLinksService {

	QuickLinks getQuickLinks();
	
	void saveQuickLinkCategory(QuickLinkCategory category);
	
	QuickLinkCategory getCategory(String categoryName);
	
	List<QuickLinkCategory> getAllCategories();
	
	void addLinkToCategory(String categoryIdentity, ContentDataRef linkContent);
	
	void removeLinkFromCategory(String linkIdentity);
	
	List<QuickLinkCategory> getAssociatedCategories(String contentIdentity);
	
	List<QuickLinkAssociationVO> getAllCategoryAssociation(String contentIdentity);
	
}

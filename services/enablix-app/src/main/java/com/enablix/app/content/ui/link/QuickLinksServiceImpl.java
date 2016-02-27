package com.enablix.app.content.ui.link;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.app.content.ui.link.repo.QuickLinkCategoryRepository;
import com.enablix.app.content.ui.link.repo.QuickLinkContentRepository;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.links.QuickLinkCategory;
import com.enablix.core.domain.links.QuickLinkContent;

@Component
public class QuickLinksServiceImpl implements QuickLinksService {

	@Autowired
	private QuickLinkContentRepository linkRepo;
	
	@Autowired
	private QuickLinkCategoryRepository categoryRepo;
	
	@Autowired
	private QuickLinkCategoryCrudService categoryCrud;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public QuickLinks getQuickLinks() {
		
		QuickLinks quickLinks = new QuickLinks();
		
		List<QuickLinkCategory> linkCategories = categoryRepo.findAll();
		
		for (QuickLinkCategory category : linkCategories) {
			quickLinks.addQuickLinkSection(category);
		}
		
		List<QuickLinkContent> allContent = linkRepo.findAll();
		
		for (QuickLinkContent linkContent : allContent) {
			
			NavigableContent navContent = navContentBuilder.build(linkContent.getData(), labelResolver);
			
			QuickLinks.Link link = new QuickLinks.Link();
			link.setCategoryIdentity(linkContent.getCategory().getIdentity());
			link.setData(navContent);
			link.setQuickLinkIdentity(linkContent.getIdentity());
			
			quickLinks.addLink(link);
		}
		
		return quickLinks;
	}

	@Override
	public void saveQuickLinkCategory(QuickLinkCategory category) {
		categoryCrud.saveOrUpdate(category);
	}

	@Override
	public QuickLinkCategory getCategory(String categoryName) {
		return categoryRepo.findByName(categoryName);
	}

	@Override
	public List<QuickLinkCategory> getAllCategories() {
		return categoryRepo.findAll();
	}

	@Override
	public QuickLinkContent addLinkToCategory(String categoryIdentity, ContentDataRef linkData) {
		
		QuickLinkCategory category = categoryRepo.findByIdentity(categoryIdentity);
		if (category == null) {
			throw new IllegalArgumentException("Quick Link category [" + categoryIdentity
					+ "] does not exist");
		}
		
		if (StringUtil.isEmpty(linkData.getTemplateId())) {
			linkData.setTemplateId(ProcessContext.get().getTemplateId());
		}
		
		QuickLinkContent linkContent = new QuickLinkContent();
		linkContent.setCategory(category);
		linkContent.setData(linkData);
		
		return linkRepo.save(linkContent);
		
	}

	@Override
	public void removeLinkFromCategory(String quickLinkContentIdentity) {
		linkRepo.deleteByIdentity(quickLinkContentIdentity);
	}

	@Override
	public List<QuickLinkCategory> getAssociatedCategories(String contentIdentity) {
		
		List<QuickLinkCategory> associatedCategories = new ArrayList<>();
		List<QuickLinkContent> contentLinks = linkRepo.findByDataInstanceIdentity(contentIdentity);

		if (contentLinks != null) {
			for (QuickLinkContent link : contentLinks) {
				associatedCategories.add(link.getCategory());
			}
		}
		
		return associatedCategories;
	}

	@Override
	public List<QuickLinkAssociationVO> getAllCategoryAssociation(String contentIdentity) {
		
		List<QuickLinkAssociationVO> associations = new ArrayList<>();
		
		List<QuickLinkCategory> allCategories = getAllCategories();
		List<QuickLinkCategory> associatedCategories = new ArrayList<>();
		
		List<QuickLinkContent> contentLinks = linkRepo.findByDataInstanceIdentity(contentIdentity);

		if (contentLinks != null) {
			for (QuickLinkContent link : contentLinks) {
				associations.add(new QuickLinkAssociationVO(link.getCategory(), true, link.getIdentity()));
				associatedCategories.add(link.getCategory());
			}
		}
		
		for (QuickLinkCategory category : allCategories) {
			if (!associatedCategories.contains(category)) {
				associations.add(new QuickLinkAssociationVO(category, false, null));
			}
		}
		
		return associations;
	}

	@Override
	public void removeLinkCategory(String quickLinkCategoryIdentity) {
		
		QuickLinkCategory quickLinkCategory = categoryRepo.findByIdentity(quickLinkCategoryIdentity);
		
		if (quickLinkCategory != null) {
			linkRepo.deleteByCategoryId(quickLinkCategory.getId());
			categoryRepo.deleteByIdentity(quickLinkCategoryIdentity);
		}
		
	}

}

package com.enablix.app.content.ui.link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.app.content.ui.link.repo.QuickLinkCategoryRepository;
import com.enablix.app.content.ui.link.repo.QuickLinkContentRepository;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.OrderAware;
import com.enablix.core.domain.links.QuickLinkCategory;
import com.enablix.core.domain.links.QuickLinkContent;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.core.domain.tenant.TenantClient;
import com.enablix.core.mongo.MongoUtil;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.security.auth.repo.ClientRepository;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;

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
	
	@Autowired
	private DataSegmentInfoBuilder dsInfoBuilder;
	
	@Autowired
	private ClientRepository clientRepo;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	private Sort sortByOrder = OrderAware.SORT_BY_ORDER;
	
	@Override
	public QuickLinks getQuickLinks(DataView dataView, String clientId) {
		List<QuickLinkCategory> linkCategories = categoryRepo.findByClientId(clientId, sortByOrder);
		return buildQuickLinks(dataView, linkCategories);
	}
	
	@Override
	public QuickLinks getQuickLinks(DataView dataView) {
		List<QuickLinkCategory> linkCategories = categoryRepo.findAll(sortByOrder);
		return buildQuickLinks(dataView, linkCategories);
	}

	private QuickLinks buildQuickLinks(DataView dataView, List<QuickLinkCategory> linkCategories) {
		
		QuickLinks quickLinks = new QuickLinks();
		
		List<TenantClient> clients = clientRepo.findAll();
		
		Map<String, TenantClient> clientIdMap = new HashMap<>();
		if (CollectionUtil.isNotEmpty(clients)) {
			for (TenantClient client : clients) {
				clientIdMap.put(client.getClientId(), client);
			}
		}
		
		
		for (QuickLinkCategory category : linkCategories) {
			TenantClient client = clientIdMap.get(category.getClientId());
			quickLinks.addQuickLinkSection(category, client == null ? null : client.getClientName());
		}
		
		MongoDataView mongoDataView = DataViewUtil.getMongoDataView(dataView);
		
		List<QuickLinkContent> allContent = MongoUtil.executeWithDataViewScope(
				mongoDataView, () -> linkRepo.findAll(sortByOrder));
				
		
		for (QuickLinkContent linkContent : allContent) {
			
			if (linkCategories.contains(linkContent.getCategory())) {
				
				NavigableContent navContent = navContentBuilder.build(linkContent.getData(), labelResolver);
				
				QuickLinks.Link link = new QuickLinks.Link();
				link.setCategoryIdentity(linkContent.getCategory().getIdentity());
				link.setData(navContent);
				link.setQuickLinkIdentity(linkContent.getIdentity());
				link.setOrder(linkContent.getOrder());
				
				quickLinks.addLink(link);
			}
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
		
		// set data segment info
		DataSegmentInfo dsInfo = dsInfoBuilder.build(linkData);
		linkContent.setDataSegmentInfo(dsInfo);
		
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

	@Override
	public boolean updateCategoryClientId(String qlCategoryName, String clientId) {
		
		QuickLinkCategory category = categoryRepo.findByName(qlCategoryName);
		
		if (category != null) {
			category.setClientId(clientId);
			categoryRepo.save(category);
			return true;
		}
		
		return false;
	}

}

package com.enablix.app.content.recent;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.recent.repo.RecentDataRepository;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.domain.recent.RecentData;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.SearchCriteria;
import com.enablix.core.mongo.search.service.SearchRequest;

@Component
public class RecentContentServiceImpl implements RecentContentService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RecentContentServiceImpl.class);

	private RecentContentContextBuilder<WebContentRequest> requestBuilder = 
			new RecentContentWebRequestBuilder();
	
	@Autowired
	private RecentDataRepository repo;
	
	@Autowired
	private GenericDao dao;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	// TODO: this should be tenant specific configuration
	@Value("${recent.data.result.size:5}")
	private int pageSize;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public List<NavigableContent> getRecentContent(WebContentRequest request) {
		RecentContentContext context = requestBuilder.build(request);
		
		Page<RecentData> recentDataList = null;
		
		String templateId = context.getRequestContext().templateId();
		
		String containerQId = context.getRequestContext().containerQId();
		String contentIdentity = context.getRequestContext().contentIdentity();
		
		Pageable pageable = createDefaultPageable();
		
		if (!StringUtil.isEmpty(containerQId) && !StringUtil.isEmpty(contentIdentity)) {
			recentDataList = repo.findByTemplateIdAndContainerQIdAndContentIdentity(
					templateId, containerQId, contentIdentity, pageable);
			
		} 
		
		if (CollectionUtil.isEmpty(recentDataList) && 
				!StringUtil.isEmpty(containerQId) && StringUtil.isEmpty(contentIdentity)) {
			recentDataList = repo.findByTemplateIdAndContainerQId(
					templateId, containerQId, pageable);
			
		} 
		
		if (CollectionUtil.isEmpty(recentDataList)) {
			recentDataList = repo.findByTemplateId(templateId, pageable);
		}

		return buildNavagableContent(recentDataList);
	}

	private List<NavigableContent> buildNavagableContent(Page<RecentData> recentDataList) {
		
		List<NavigableContent> navRecentData = new ArrayList<>();

		if (recentDataList != null) {
			
			for (RecentData recentData : recentDataList) {
			
				NavigableContent navContent = createNavigableContent(recentData);
				if (navContent != null) {
					navRecentData.add(navContent);
				}
			}
		}
		
		return navRecentData;
	}

	private NavigableContent createNavigableContent(RecentData recentData) {
		
		NavigableContent navContent = navContentBuilder.build(recentData.getData(), labelResolver);
		
		if (navContent != null) {
			navContent.getAdditionalInfo().put("updateType", recentData.getUpdateType().toString());
		} else {
			LOGGER.error("Unable to build nav content for recent data [{}]", recentData.getIdentity());
		}
		
		return navContent;
	}

	private Pageable createDefaultPageable() {
		Sort sort = new Sort(Sort.Direction.DESC, ContentDataConstants.CREATED_AT_KEY);
		Pageable pageable = new PageRequest(0, pageSize, sort);
		return pageable;
	}
	
	@Override
	public List<NavigableContent> getRecentContentByCriteria(SearchCriteria criteria) {
		return getRecentContentByCriteriaAndPageable(criteria, createDefaultPageable());
	}
	
	private List<NavigableContent> getRecentContentByCriteriaAndPageable(SearchCriteria criteria, Pageable pageable) {
		Page<RecentData> contentPage = dao.findByCriteria(
				criteria.toPredicate(new Criteria()), RecentData.class, pageable);
		return buildNavagableContent(contentPage);
	}

	@Override
	public Page<RecentUpdateVO> getRecentContentByRequest(SearchRequest request) {
	
		List<RecentUpdateVO> recentList = new ArrayList<>();
		Page<RecentData> contentPage = dao.findByQuery(request, RecentData.class);
		
		for (RecentData recentData : contentPage) {
			recentList.add(new RecentUpdateVO(recentData, createNavigableContent(recentData)));
		}
		
		Pageable pageable = request.getPagination().toPageableObject();
		return new PageImpl<>(recentList, pageable, contentPage.getTotalElements());
	}
	

}

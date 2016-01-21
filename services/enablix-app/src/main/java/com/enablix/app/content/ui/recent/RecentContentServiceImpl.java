package com.enablix.app.content.ui.recent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

@Component
public class RecentContentServiceImpl implements RecentContentService {

	private RecentContentContextBuilder<WebContentRequest> requestBuilder = 
			new RecentContentWebRequestBuilder();
	
	@Autowired
	private RecentDataRepository repo;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public List<NavigableContent> getRecentContent(WebContentRequest request) {
		
		RecentContentContext context = requestBuilder.build(request);
		
		Collection<RecentData> recentDataList = null;
		
		String templateId = context.getRequestContext().templateId();
		
		String containerQId = context.getRequestContext().containerQId();
		String contentIdentity = context.getRequestContext().contentIdentity();
		
		Sort sort = new Sort(Sort.Direction.DESC, ContentDataConstants.CREATED_AT_KEY);
		
		if (!StringUtil.isEmpty(containerQId) && !StringUtil.isEmpty(contentIdentity)) {
			recentDataList = repo.findByTemplateIdAndContainerQIdAndContentIdentity(
					templateId, containerQId, contentIdentity, sort);
			
		} 
		
		if (CollectionUtil.isEmpty(recentDataList) && 
				!StringUtil.isEmpty(containerQId) && StringUtil.isEmpty(contentIdentity)) {
			recentDataList = repo.findByTemplateIdAndContainerQId(
					templateId, containerQId, sort);
			
		} 
		
		if (CollectionUtil.isEmpty(recentDataList)) {
			recentDataList = repo.findByTemplateId(templateId, sort);
		}

		List<NavigableContent> navRecentData = new ArrayList<>();

		if (recentDataList != null) {
			for (RecentData recentData : recentDataList) {
				NavigableContent navContent = navContentBuilder.build(recentData.getData(), labelResolver);
				navContent.getAdditionalInfo().put("updateType", recentData.getUpdateType().toString());
				navRecentData.add(navContent);
			}
		}

		return navRecentData;
	}

}

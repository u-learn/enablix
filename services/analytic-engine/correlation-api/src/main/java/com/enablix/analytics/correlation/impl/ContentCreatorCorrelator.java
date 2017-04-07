package com.enablix.analytics.correlation.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.CorrelationConstants;
import com.enablix.analytics.correlation.ItemUserCorrelator;
import com.enablix.analytics.correlation.context.CorrelationContext;
import com.enablix.app.content.ContentDataManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.services.util.DataViewUtil;

@Component
public class ContentCreatorCorrelator implements ItemUserCorrelator {

	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private ItemUserCorrelationRecorder userCorrRecorder;
	
	@Override
	public void correlateUsers(ContentDataRef item, CorrelationContext context) {
		
		TemplateFacade template = context.getTemplate();
		
		Map<String, Object> contentRecord = contentDataMgr.getContentRecord(item, template, DataViewUtil.allDataView());
		String creatorEmail = (String) contentRecord.get(ContentDataConstants.CREATED_BY_KEY);
		
		UserProfile creator = userProfileRepo.findByEmail(creatorEmail);
		
		if (creator != null) {
			
			List<String> tags = new ArrayList<>();
			tags.add(CorrelationConstants.CONTENT_CREATOR_ITEM_USER_CORR_TAG);
			
			userCorrRecorder.recordItemUserCorrelation(item, creator, tags);
		}
		
	}

	@Override
	public void deleteCorrelationForItem(ContentDataRef item) {
		userCorrRecorder.removeItemUserCorrelation(item);
	}

}

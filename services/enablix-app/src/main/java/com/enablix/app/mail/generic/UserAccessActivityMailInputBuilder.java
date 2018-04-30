package com.enablix.app.mail.generic;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.activity.Activity;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ContentAccessActivity;
import com.enablix.core.domain.activity.DocumentActivity;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mail.entities.EmailRequest;
import com.enablix.core.mail.velocity.AbstractEmailVelocityInputBuilder;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.service.SearchRequestTransformer;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;

@Component
public class UserAccessActivityMailInputBuilder extends AbstractEmailVelocityInputBuilder<UserContentAccessActivityMailInput> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAccessActivityMailInputBuilder.class);
	
	@Autowired
	private GenericDao dao;
	
	@Autowired
	private SearchRequestTransformer requestTx;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public String mailType() {
		return GenericMailConstants.MAIL_TYPE_USER_CONTENT_ACCESS;
	}

	@Override
	protected UserContentAccessActivityMailInput buildInput(UserContentAccessActivityMailInput input,
			EmailRequest request, DataView dataView) {
		
		Object actorUserId = request.getInputData().get("userId");
		
		if (actorUserId == null) {
			LOGGER.error("Required input [userId] missing");
			throw new IllegalArgumentException("Required input [userId] missing");
		}
		
		UserProfile actorProfile = userProfileRepo.findByEmail(String.valueOf(actorUserId));
		
		if (actorProfile == null) {
			LOGGER.error("Invalid user id [{}]", actorUserId);
			throw new IllegalArgumentException("Invalid user id [" + actorUserId + "]");
		}
		
		input.setActor(actorProfile);
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		Criteria criteria = requestTx.buildQueryCriteria(request.getDataFilter());
		
		Set<String> accessContentIdentities = new HashSet<>(); // list to eliminate duplicate records
		
		List<ActivityAudit> accessActivity = dao.findByCriteria(criteria, ActivityAudit.class, DataViewUtil.getMongoDataView(dataView));
		
		for (ActivityAudit actAudit : accessActivity) {

			Activity activity = actAudit.getActivity();
			
			if (activity instanceof ContentAccessActivity) {
			
				ContentDataRef contentRef = createContentRef((ContentAccessActivity) activity, dataView, template);
				
				if (contentRef != null) {
					accessContentIdentities.add(contentRef.getInstanceIdentity());
					input.addContentAccess(contentRef, template);
				}
			}
			
		}

		return input;
	}
	
	private ContentDataRef createContentRef(ContentAccessActivity activity, DataView view, TemplateFacade template) {
		
		if (activity instanceof DocumentActivity) {
			return createContentRefForDocActivity((DocumentActivity) activity, view, template);
		}
		
		return ContentDataRef.createContentRef(template.getId(), 
				activity.getContainerQId(), activity.getItemIdentity(), activity.getItemTitle());
	}

	private ContentDataRef createContentRefForDocActivity(DocumentActivity docDownload, DataView view, TemplateFacade template) {
		
		String docElemQId = docDownload.getContainerQId();
		ContentDataRef content = null;
		
		Map<String, Object> contentRec = contentDataMgr.getContentRecordByDocRef(docDownload.getDocIdentity(), docElemQId, view);
		
		if (CollectionUtil.isNotEmpty(contentRec)) {
			
			String containerQId = QIdUtil.getParentQId(docElemQId);
			String itemTitle = ContentDataUtil.findPortalLabelValue(contentRec, template, containerQId);
			String recordIdentity = ContentDataUtil.getRecordIdentity(contentRec);
			
			content = ContentDataRef.createContentRef(template.getId(), containerQId, recordIdentity, itemTitle);
		}
		
		return content;
	}

	@Override
	protected UserContentAccessActivityMailInput createInputInstance() {
		return new UserContentAccessActivityMailInput();
	}

}

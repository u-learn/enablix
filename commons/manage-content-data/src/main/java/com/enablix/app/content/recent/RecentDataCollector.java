package com.enablix.app.content.recent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.event.ContentDataDelEvent;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.app.content.recent.repo.RecentDataRepository;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.recent.RecentData;
import com.enablix.core.domain.recent.RecentData.UpdateType;
import com.enablix.core.domain.recent.RecentDataScope;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class RecentDataCollector implements ContentDataEventListener {

	@Autowired
	private RecentDataRepository recentDataRepo;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {

		RecentData recentData = new RecentData();
		Object contentIdentity = event.getDataAsMap().get(ContentDataConstants.IDENTITY_KEY);
		
		if (contentIdentity != null && !event.getContainerType().isRefData()) {
			
			if (event.isNewRecord()) {
			
				createNewRecord(event, recentData, contentIdentity, UpdateType.NEW);
				
			} else {
				
				String strContentIdentity = String.valueOf(contentIdentity);
				RecentData existEntry = recentDataRepo.findByDataInstanceIdentityAndUpdateType(
											strContentIdentity, UpdateType.UPDATED);
				
				if (existEntry == null) {
					
					createNewRecord(event, recentData, contentIdentity, UpdateType.UPDATED);
					markExistNewTypeRecordAsObsolete(strContentIdentity);
					
				} else {
					// clear created at date and simply save it again, create date will be updated
					// the modified at date of the NEW type record is updated as we update the
					// obsolete flag. As a result of which, the NEW record starts showing along with 
					// the updated recent records as we query by modified date. We will now use the
					// created date for sorting instead of modified date
					existEntry.setCreatedAt(null);
					existEntry.setCreatedBy(null);
					recentDataRepo.save(existEntry);
				}
			}
		}
	}

	private void markExistNewTypeRecordAsObsolete(String contentIdentity) {
		
		RecentData newRecord = recentDataRepo.findByDataInstanceIdentityAndUpdateType(
				contentIdentity, UpdateType.NEW);
		
		if (newRecord != null) {
			newRecord.setObsolete(true);
			recentDataRepo.save(newRecord);
		}
	}
	
	private void createNewRecord(ContentDataSaveEvent event, RecentData recentData, 
			Object contentIdentity, UpdateType updateType) {
		
		TemplateWrapper templateWrapper = templateMgr.getTemplateWrapper(event.getTemplateId());
		String contentQId = event.getContainerType().getQualifiedId();
		String contentTitle = ContentDataUtil.findPortalLabelValue(event.getDataAsMap(), templateWrapper, contentQId);
		
		ContentDataRef dataRef = ContentDataRef.createContentRef(event.getTemplateId(), 
				contentQId, String.valueOf(contentIdentity), contentTitle);
		
		// set data ref
		recentData.setData(dataRef);
		
		// set scope of data ref
		RecentDataScope scope = new RecentDataScope();
		scope.setTemplateId(event.getTemplateId());

		recentData.setScope(scope);
		
		// set update type
		recentData.setUpdateType(updateType);
		
		recentDataRepo.save(recentData);
	}

	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		recentDataRepo.deleteByDataInstanceIdentity(event.getContentIdentity());
	}

}

package com.enablix.app.content.recent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.recent.repo.RecentDataRepository;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.content.event.ContentDataDelEvent;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.domain.recent.RecentData;
import com.enablix.core.domain.recent.RecentData.UpdateType;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;
import com.enablix.core.domain.recent.RecentDataScope;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.services.util.ContentDataUtil;

@Component
public class RecentDataCollector implements ContentDataEventListener {

	@Autowired
	private RecentDataRepository recentDataRepo;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private DataSegmentInfoBuilder dsInfoBuilder;
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {

		RecentData recentData = new RecentData();
		
		Object contentIdentity = event.getDataAsMap().get(ContentDataConstants.IDENTITY_KEY);
		DataSegmentInfo dsInfo = getDataSegmentInfo(event);
		
		if (contentIdentity != null && !event.getContainerType().isRefData()) {
			
			recentData.setDataSegmentInfo(dsInfo);
			
			if (event.isNewRecord()) {
			
				createNewRecord(event, recentData, contentIdentity, UpdateType.NEW);
				
			} else {
				
				String strContentIdentity = String.valueOf(contentIdentity);
				RecentData existEntry = recentDataRepo.findByDataInstanceIdentityAndUpdateType(
											strContentIdentity, UpdateType.UPDATED);
				
				TemplateFacade templateWrapper = templateMgr.getTemplateFacade(event.getTemplateId());
				
				if (existEntry == null) {
					
					createNewRecord(event, recentData, contentIdentity, UpdateType.UPDATED);
					markExistNewTypeRecordAsObsoleteAndUpdateDSInfo(strContentIdentity, dsInfo);
					
				} else {

					// clear created at date and simply save it again, create date will be updated
					// the modified at date of the NEW type record is updated as we update the
					// obsolete flag. As a result of which, the NEW record starts showing along with 
					// the updated recent records as we query by modified date. We will now use the
					// created date for sorting instead of modified date
					
					existEntry.setCreatedAt(null);
					existEntry.setCreatedBy(null);
					
					String contentQId = event.getContainerType().getQualifiedId();
					String contentTitle = ContentDataUtil.findPortalLabelValue(event.getDataAsMap(), templateWrapper, contentQId);
					
					existEntry.getData().setTitle(contentTitle);
					existEntry.setDataSegmentInfo(dsInfo);
					recentDataRepo.save(existEntry);
					
					markExistNewTypeRecordAsObsoleteAndUpdateDSInfo(strContentIdentity, dsInfo);
				}
			}
		}
	}

	private void markExistNewTypeRecordAsObsoleteAndUpdateDSInfo(String contentIdentity, DataSegmentInfo dsInfo) {
		
		RecentData newRecord = recentDataRepo.findByDataInstanceIdentityAndUpdateType(
				contentIdentity, UpdateType.NEW);
		
		if (newRecord != null) {
			newRecord.setObsolete(true);
			newRecord.setDataSegmentInfo(dsInfo);
			recentDataRepo.save(newRecord);
		}
	}
	
	private void createNewRecord(ContentDataSaveEvent event, RecentData recentData, 
			Object contentIdentity, UpdateType updateType) {
		
		TemplateFacade templateWrapper = templateMgr.getTemplateFacade(event.getTemplateId());
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

	private DataSegmentInfo getDataSegmentInfo(ContentDataSaveEvent event) {
		String contentQId = event.getContainerType().getQualifiedId();
		return dsInfoBuilder.build(
				new ContentDataRecord(event.getTemplateId(), contentQId, event.getDataAsMap()));
	}

	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		recentDataRepo.deleteByDataInstanceIdentity(event.getContentIdentity());
	}

}

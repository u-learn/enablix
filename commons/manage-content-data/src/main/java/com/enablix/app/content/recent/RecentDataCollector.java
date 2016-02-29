package com.enablix.app.content.recent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.event.ContentDataDelEvent;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.app.content.recent.repo.RecentDataRepository;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.recent.RecentData;
import com.enablix.core.domain.recent.RecentData.UpdateType;
import com.enablix.core.domain.recent.RecentDataScope;

@Component
public class RecentDataCollector implements ContentDataEventListener {

	@Autowired
	private RecentDataRepository recentDataRepo;
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {

		RecentData recentData = new RecentData();
		Object contentIdentity = event.getDataAsMap().get(ContentDataConstants.IDENTITY_KEY);
		
		if (contentIdentity != null && !event.getContainerType().isRefData()) {
			
			if (event.isNewRecord()) {
			
				createNewRecord(event, recentData, contentIdentity, UpdateType.NEW);
				
			} else {
				
				RecentData existEntry = recentDataRepo.findByDataInstanceIdentityAndUpdateType(
											String.valueOf(contentIdentity), UpdateType.UPDATED);
				
				if (existEntry == null) {
					
					createNewRecord(event, recentData, contentIdentity, UpdateType.UPDATED);
					
				} else {
					// simply save it again, it will update the updated date
					recentDataRepo.save(existEntry);
				}
			}
		}
	}

	private void createNewRecord(ContentDataSaveEvent event, RecentData recentData, 
			Object contentIdentity, UpdateType updateType) {
		
		ContentDataRef dataRef = new ContentDataRef(event.getTemplateId(), 
				event.getContainerType().getQualifiedId(), String.valueOf(contentIdentity));
		
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

package com.enablix.data.segment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.segment.repo.DataSegmentRepository;
import com.enablix.data.segment.view.DataViewBuilder;
import com.enablix.data.view.DataView;

@Component
public class DataSegmentServiceImpl implements DataSegmentService {

	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private DataSegmentRepository dsRepo;
	
	@Autowired
	private DataViewBuilder dataViewBuilder;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public DataView getDataViewForUserId(String userId) {
		UserProfile userProfile = userProfileRepo.findByEmail(userId);
		return createDataViewForUser(userProfile);
	}

	private DataView createDataViewForUser(UserProfile userProfile) {
		
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		return dataViewBuilder.createDataView(userProfile.getSystemProfile().getDataSegment(), template);
		
		/*return new DataView() {
			
			@SuppressWarnings("unchecked")
			@Override
			public <T extends DatastoreView> T getDatastoreView(String storeType, Class<T> viewType) {
				
				if (AppConstants.MONGO_DATASTORE.equals(storeType)) {
					return (T) MongoDataView.ALL_DATA;
				} else if (AppConstants.ELASTICSEARCH_DATASTORE.equals(storeType)) {
					return (T) ESDataView.ALL_DATA;
				}
				
				throw new IllegalArgumentException("Unsupported data store: " + storeType);
			}
			
		};*/
	}

	@Override
	public DataView getDataViewForUserProfileIdentity(String userProfileIdentity) {
		UserProfile userProfile = userProfileRepo.findByIdentity(userProfileIdentity);
		return createDataViewForUser(userProfile);
	}

	@Override
	public void save(DataSegment ds) {
		dsRepo.save(ds);
	}

	
	
}

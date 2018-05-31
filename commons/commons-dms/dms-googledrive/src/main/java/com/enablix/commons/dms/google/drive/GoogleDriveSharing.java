package com.enablix.commons.dms.google.drive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.commons.dms.DocumentStoreConstants;
import com.enablix.commons.util.PermissionConstants;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.domain.config.Configuration;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mq.EventSubscription;
import com.enablix.core.security.SecurityUtil;
import com.enablix.core.security.auth.repo.UserProfileRepository;

@Component
public class GoogleDriveSharing {

	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDriveSharing.class);
	
	@Autowired
	private GoogleDriveDocumentStore docStore;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	
	@EventSubscription(eventName = {Events.USER_ADDED})
	public void shareDocstoreWithUser(UserProfile userProfile) {
		
		if (isGoogleDriveDefaultStore()) {

			try {
				shareWithUser(userProfile);
			} catch (IOException e) {
				LOGGER.error("Error share doc store with user: " + userProfile.getEmail(), e);
			} 
		}
	}
	
	@EventSubscription(eventName = {Events.USER_UPDATED})
	public void unshareDocstoreWithUser(UserProfile userProfile) {
		
		if (isGoogleDriveDefaultStore()) {

			try {
				
				if (!SecurityUtil.userHasAllPermission(userProfile, 
						PermissionConstants.PERMISSION_DOCSTORE_DIRECT_ACCESS)) {
					docStore.unshareWithUser(userProfile.getEmail());
				} else {
					docStore.checkAndShareWithUser(userProfile.getEmail());
				}
				
			} catch (IOException e) {
				LOGGER.error("Error share doc store with user: " + userProfile.getEmail(), e);
			} 
		}
	}
	
	private void shareWithUser(UserProfile userProfile) throws FileNotFoundException, IOException {
		
		if (SecurityUtil.userHasAllPermission(userProfile, 
				PermissionConstants.PERMISSION_DOCSTORE_DIRECT_ACCESS)) {
			docStore.shareWithUser(userProfile.getEmail());
		}
	}
	
	@EventSubscription(eventName = {Events.DOC_STORE_CONFIG_UPDATED})
	public void shareDocstoreWithAllUsers(Configuration docStoreConfig) {
		
		if (isGoogleDriveDefaultStore()) {
			
			try {
				
				List<UserProfile> allUsers = userProfileRepo.findAll();
				
				for (UserProfile userProfile : allUsers) {
					shareWithUser(userProfile);
				}
				
			} catch (IOException e) {
				LOGGER.error("Error share doc store with users ", e);
			} 
		}
	}
	
	private boolean isGoogleDriveDefaultStore() {
		
		Configuration docStoreConfig = ConfigurationUtil.getConfig(DocumentStoreConstants.DOC_STORE_CONFIG_KEY);
		
		if (docStoreConfig != null) {
			String storeType = docStoreConfig.getStringValue(DocumentStoreConstants.DEFAULT_DOC_STORE_CONFIG_PROP);
			return GoogleDriveDocumentStore.DOC_STORE_TYPE.equals(storeType);
		}
		
		return false;
	}
	
}

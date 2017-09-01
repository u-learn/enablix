package com.enablix.app.mail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.share.ShareContentConstants;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.security.authorization.UserBusinessProfile;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.security.authorization.UserSystemProfile;
import com.enablix.core.domain.share.SharedSiteUrl;
import com.enablix.core.domain.user.User;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.core.security.service.GuestUserProvider;
import com.enablix.core.system.repo.SharedSiteUrlRepository;
import com.enablix.core.system.repo.UserRepository;

@Component
public class SharedContentGuestUserProvider implements GuestUserProvider {

	@Autowired
	private SharedSiteUrlRepository sharedUrlRepo;
	
	@Autowired 
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	private List<String> urls = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		urls.add(ShareContentConstants.UNSECURE_SHARE_URL_PREFIX + "/*");
	}
	
	@Override
	public User getGuestUser(HttpServletRequest request) {
		
		User user = null;
		
		SharedSiteUrl shareDetails = getSharedUrlDetails(request);
		
		if (shareDetails != null) {
			
			user = userRepo.findByUserIdAndTenantId(
					shareDetails.getSharedWith(), shareDetails.getTenantId());
			
			if (user == null) {
				user = new User();
				user.setIdentity(AppConstants.GUEST_USER_IDENTITY);
				user.setUserId(shareDetails.getSharedWith());
				user.setTenantId(shareDetails.getTenantId());
			}
		}
		
		return user;
	}
	
	private UserProfile getUserProfile(User user) {
		
		UserProfile up = null;
		
		try {
			
			ProcessContext.initialize(user.getUserId(), user.getUserId(), user.getTenantId(), null, null);
			up = userProfileRepo.findByUserIdentity(user.getIdentity());
			
		} finally {
			ProcessContext.clear();
		}
		
		return up;
	}

	@Override
	public UserProfile getGuestUserProfile(User user) {
		
		UserProfile usrProfile = new UserProfile();
		
		if (user != null) {

			if (!AppConstants.GUEST_USER_IDENTITY.equals(user.getIdentity())) {
				usrProfile = getUserProfile(user);
			}
			
			if (usrProfile == null) {
				usrProfile = new UserProfile();
				usrProfile.setIdentity(AppConstants.GUEST_USER_IDENTITY);
				usrProfile.setUserIdentity(AppConstants.GUEST_USER_IDENTITY);
				usrProfile.setEmail(user.getUserId());
				usrProfile.setName(user.getUserId());
			}

			// set business profile and system profile as empty as we do not want the user
			// to perform any other activity on portal without login. Only shared content should be
			// allowed to be downloaded etc.
			usrProfile.setBusinessProfile(new UserBusinessProfile());
			usrProfile.setSystemProfile(new UserSystemProfile());

		}
		
		return usrProfile;
	}
	
	private SharedSiteUrl getSharedUrlDetails(HttpServletRequest request) {
		String sharedUrl = request.getRequestURI();
		return sharedUrlRepo.findBySharedUrl(sharedUrl);
	}

	@Override
	public List<String> supportedRequestUrls() {
		return urls;
	}

}

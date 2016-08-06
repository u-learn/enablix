package com.enablix.app.mail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.share.SharedSiteUrl;
import com.enablix.core.domain.user.User;
import com.enablix.core.security.service.GuestUserProvider;
import com.enablix.core.security.web.UserAndRolesVO;
import com.enablix.core.system.repo.SharedSiteUrlRepository;

@Component
public class SharedContentGuestUserProvider implements GuestUserProvider {

	@Autowired
	private SharedSiteUrlRepository sharedUrlRepo;
	
	private List<String> urls = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		urls.add(ShareContentConstants.UNSECURE_SHARE_URL_PREFIX + "/*");
	}
	
	@Override
	public UserAndRolesVO getGuestUser(HttpServletRequest request) {
		
		UserAndRolesVO userAndRoles = null;
		
		SharedSiteUrl shareDetails = getSharedUrlDetails(request);
		
		if (shareDetails != null) {
			userAndRoles = new UserAndRolesVO();
			
			User user = new User();
			user.setIdentity(AppConstants.GUEST_USER_IDENTITY);
			user.setUserId(shareDetails.getSharedWith());
			user.setTenantId(shareDetails.getTenantId());
			
			userAndRoles.setUser(user);
		}
		
		return userAndRoles;
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

package com.enablix.user.mgmt.web;

import java.io.IOException;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.preference.UserPreference;
import com.enablix.user.pref.UserPreferenceService;

@RestController
@RequestMapping("userpref")
public class UserPreferenceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPreferenceController.class);
	
	@Autowired
	private UserPreferenceService userPrefService;
	
	@RequestMapping(method = RequestMethod.GET, value="/applicable", produces = "application/json")
	public Collection<UserPreference> getApplicableUserPreferences() throws IOException, JAXBException {
		
		LOGGER.debug("Fetching applicable preference for the user");
		
		Collection<UserPreference> prefs = null;
		
		String userId = ProcessContext.get().getUserId();
		if (StringUtil.hasText(userId)) {
			prefs = userPrefService.userApplicablePreferences(userId);
		}
		
		return prefs;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/saveasuserpref", produces = "application/json")
	public Boolean saveAsUserPreference(@RequestBody UserPreference userPref) throws IOException, JAXBException {
		
		LOGGER.debug("Saving user preference");
		
		userPref.setUserId(ProcessContext.get().getUserId());
		userPrefService.saveUserPreference(userPref);
		
		return Boolean.TRUE;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/saveasyspref", produces = "application/json")
	public Boolean saveAsSystemPreference(@RequestBody UserPreference userPref) throws IOException, JAXBException {
		
		LOGGER.debug("Saving user preference");
		
		userPref.setUserId(AppConstants.SYSTEM_USER_ID);
		userPrefService.saveUserPreference(userPref);
		
		return Boolean.TRUE;
	}
	
}

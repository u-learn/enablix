package com.enablix.tenant.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.exception.AppError;
import com.enablix.commons.exception.ErrorCodes;
import com.enablix.commons.exception.ValidationException;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.SudoExecutor;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.SignupRequest;
import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.security.authorization.UserSystemProfile;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.security.service.EnablixUserService;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.tenant.TenantManager;
import com.enablix.tenant.TenantSetupTask;

@Component
public class TenantManagerImpl implements TenantManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantManagerImpl.class);
	
	@Value("#{'${signup.user.roles:contentAdmin,portalUser}'.split(',')}")
	private List<String> signUpUserRoleIds;
	
	@Value("${new.tenant.default.portal:v2}")
	private String tenantPortal;
	
	@Autowired 
	private EnablixUserService userService;
	
	@Autowired
	private TenantRepository tenantRepo;
	
	@Autowired
	private TenantSetupTaskFactory setupTaskFactory;
	
	@Override
	public void setupTenant(SignupRequest request) throws Exception {
		
		/*
		 * 1. Check if user exists
		 * 2. Create tenantId
		 * 3. Create tenant record
		 * 4. Copy mongodb reference data
		 * 5. Copy content template 
		 * 6. Create sample data records
		 * 7. Any other tenant setup task
		 * 8. Create user
		 */
		
		Boolean userExist = userService.checkUserByUserId(request.getEmail());
		
		if (userExist) {
			
			LOGGER.error("User [{}] already exists in the system", request.getEmail());
			
			throw new ValidationException(Collections.singletonList(
					new AppError(ErrorCodes.USER_ALREADY_EXIST, "User already exists in the system")));
		}
		
		Tenant tenant = createTenantRecord(request);
		
		ProcessContext.clear();
		ProcessContext.initialize(AppConstants.SYSTEM_USER_ID, AppConstants.SYSTEM_USER_NAME, 
				tenant.getTenantId(), tenant.getDefaultTemplateId(), null);

		try {

			// run set up tasks
			for (TenantSetupTask setupTask : setupTaskFactory.getAllTasks()) {
				setupTask.execute(tenant);
			}
			
			// create user
			createUser(request);
			
		} finally {
			ProcessContext.clear();
		}
		
	}

	private void createUser(SignupRequest request) {
		
		UserProfile userProfile = new UserProfile();
		userProfile.setEmail(request.getEmail());
		userProfile.setName(request.getName());
		
		List<Role> roles = new ArrayList<>();
		signUpUserRoleIds.forEach((roleId) -> { 
			Role role = new Role();
			role.setIdentity(roleId);
			roles.add(role);
		});
		
		UserSystemProfile systemProfile = new UserSystemProfile();
		systemProfile.setRoles(roles);
		
		userProfile.setSystemProfile(systemProfile);
		userService.signupUser(userProfile, request.getPassword());
	}
	
	private Tenant createTenantRecord(SignupRequest request) {
		
		String tenantId = getTenantId(request);
		TenantCreator tenantCreator = new TenantCreator(request, tenantId);

		SudoExecutor.runAsSystem(tenantCreator);
		
		return tenantCreator.tenant;
	}
	
	private class TenantCreator implements SudoExecutor.Task {

		private Tenant tenant;
		
		TenantCreator(SignupRequest request, String tenantId) {
			
			tenant = new Tenant();
			tenant.setId(tenantId);
			tenant.setTenantId(tenantId);
			tenant.setIdentity(tenantId);
			tenant.setName(request.getOrgName());
			tenant.setDefaultTemplateId(tenantId + "_enterprise");
			tenant.setPortal(tenantPortal);
		}
		
		@Override
		public void execute() {
			tenant = tenantRepo.save(tenant); 
			
		}
		
	}
	
	private String getTenantId(SignupRequest request) {
		
		String tenantId = "tnt";
		int tenantSuffix = 1;
		
		String orgName = request.getOrgName();
		
		if (StringUtil.hasText(orgName)) {
			
			int spaceIndx = orgName.indexOf(' ');
			
			if (spaceIndx > 0) {
				tenantId = orgName.substring(0, spaceIndx); 
			} else {
				tenantId = orgName;
			}
			
			tenantId = tenantId.toLowerCase().trim();
			tenantSuffix = orgName.equals("system") ? 1 : 0;
		}
		
		return checkAndGetTenantId(tenantId, tenantSuffix);
	}

	private String checkAndGetTenantId(String tenantId, int counter) {
		
		Tenant existTenant = null;
		String finalTenantId = counter == 0 ? tenantId : (tenantId + counter);
		
		existTenant = tenantRepo.findByTenantId(finalTenantId);
		if (existTenant != null) {
			finalTenantId = checkAndGetTenantId(tenantId, ++counter);
		}
		
		return finalTenantId;
	}

}

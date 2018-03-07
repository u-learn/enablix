package com.enablix.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.api.RestResponse;
import com.enablix.core.api.SignupRequest;
import com.enablix.core.security.SecurityUtil;
import com.enablix.core.security.service.EnablixUserService;
import com.enablix.tenant.TenantManager;

@RestController
public class SignupController {
	
	@Autowired
	private TenantManager tenantManager;
	
	@Autowired
	private EnablixUserService userService;
	
	@RequestMapping(method = RequestMethod.POST, value="/signup")
	public RestResponse signup(@RequestBody SignupRequest request) throws Exception {
		
		tenantManager.setupTenant(request);
		
		UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
		SecurityUtil.loginUser(userDetails);
		
		return RestResponse.success();
	}
	
	
}

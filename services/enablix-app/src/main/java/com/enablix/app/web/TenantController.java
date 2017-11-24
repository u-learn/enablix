package com.enablix.app.web;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.system.repo.TenantRepository;

@RestController
@RequestMapping("/tenant")
public class TenantController {
	
	@Autowired
	private TenantRepository tenantRepo;
	
	@RequestMapping(method = RequestMethod.GET, value="/fetch")
	public Tenant getTenant() throws ServletException, IOException {
		return tenantRepo.findByTenantId(ProcessContext.get().getTenantId());
	}
	
}

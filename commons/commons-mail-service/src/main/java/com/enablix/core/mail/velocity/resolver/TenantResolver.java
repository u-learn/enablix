package com.enablix.core.mail.velocity.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.TenantAware;
import com.enablix.core.system.repo.TenantRepository;

@Component
public class TenantResolver implements VelocityTemplateInputResolver<TenantAware> {

	@Autowired
	private TenantRepository tenantRepo;
	
	@Override
	public void work(TenantAware velocityTemplateInput) {
		Tenant tenant = tenantRepo.findByTenantId(ProcessContext.get().getTenantId());
		if (tenant != null) {
			velocityTemplateInput.setTenant(tenant);
		}
		
	}

	@Override
	public boolean canHandle(Object velocityTemplateInput) {
		return velocityTemplateInput instanceof TenantAware;
	}

	@Override
	public float executionOrder() {
		return VelocityResolverExecOrder.TENANT_EXEC_ORDER;
	}

}

package com.enablix.core.mail.velocity.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.EnvPropertiesAware;
import com.enablix.core.system.repo.TenantRepository;

@Component
public class EnvPropertiesResolver implements VelocityTemplateInputResolver<EnvPropertiesAware> {

	@Autowired
	private TenantRepository tenantRepo;
	@Value("${server.url}")
	private String url;
	
	@Override
	public void work(EnvPropertiesAware velocityTemplateInput) {
			velocityTemplateInput.setUrl(url);
		}

	@Override
	public boolean canHandle(Object velocityTemplateInput) {
		return velocityTemplateInput instanceof EnvPropertiesAware;
	}

}
	
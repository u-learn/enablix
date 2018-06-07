package com.enablix.core.mail.velocity.resolver;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.EnvPropertiesUtil;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.input.EnvPropertiesAware;
import com.enablix.data.view.DataView;

@Component
public class EnvPropertiesResolver implements VelocityTemplateInputResolver<EnvPropertiesAware> {

	@Override
	public void work(EnvPropertiesAware velocityTemplateInput, DataView view) {
		velocityTemplateInput.setUrl(EnvPropertiesUtil.getSubdomainSpecificServerUrl());
	}

	@Override
	public boolean canHandle(Object velocityTemplateInput) {
		return velocityTemplateInput instanceof EnvPropertiesAware;
	}

	@Override
	public float executionOrder() {
		return VelocityResolverExecOrder.ENV_PROP_EXEC_ORDER;
	}

}
	
package com.enablix.content.quality.rule;

import com.enablix.core.api.TemplateFacade;

public interface TemplateConfiguredRule {

	void onTemplateUpdate(TemplateFacade template);
	
}

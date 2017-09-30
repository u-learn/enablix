package com.enablix.analytics.info.detection.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.info.detection.InfoDetectionConfiguration;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.InfoTypeResolver;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;

public class DefaultInfoTypeResolver implements InfoTypeResolver {

	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public List<TypeOpinion> resolve(InfoDetectionContext ctx) {

		List<TypeOpinion> opinions = null;
		InfoDetectionConfiguration config = ctx.getInfoDetectionConfig();
		
		if (config != null) {
			
			String defaultType = config.getDefaultContentTypeQId();
			
			if (StringUtil.hasText(defaultType)) {
				
				TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
				ContainerType containerDef = template.getContainerDefinition(defaultType);
				
				if (containerDef != null) {

					opinions = new ArrayList<>();
					
					TypeOpinion typeOpinion = new TypeOpinion(defaultType, name(), 0.5f);
					opinions.add(typeOpinion);
				}
			}
		}
		
		return opinions;
	}

	@Override
	public String name() {
		return "Default Info Type resolver";
	}
	
}

package com.enablix.analytics.info.detection.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.info.detection.EnablixContentTypeAware;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.InfoTypeResolver;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;

public class TypeNameInfoResolver implements InfoTypeResolver  {

	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public List<TypeOpinion> resolve(InfoDetectionContext ctx) {
		
		List<TypeOpinion> opinions = null;
		
		Information info = ctx.getInformation();
		
		if (info instanceof EnablixContentTypeAware) {
		
			EnablixContentTypeAware typeAwareInfo = (EnablixContentTypeAware) info;
			String enablixTypeName = typeAwareInfo.enablixTypeName();
			
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			
			ContainerType container = template.getContainerByLabel(enablixTypeName);
			
			if (container != null) {

				opinions = new ArrayList<>();
				
				TypeOpinion typeOpinion = new TypeOpinion(container.getQualifiedId(), name(), 0.75f);
				opinions.add(typeOpinion);
			}
		}
		
		return opinions;
	}

	@Override
	public String name() {
		return "Enablix Typename Info Type Resolver";
	}

}

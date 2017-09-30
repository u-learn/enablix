package com.enablix.analytics.info.detection.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import com.enablix.analytics.info.detection.InfoDetectionConfiguration;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.InfoTypeResolver;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.LinkAwareInfo;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;

public class InfoLinkPatternBasedTypeResolver implements InfoTypeResolver {
	
	@Autowired
	private TemplateManager templateMgr;
	
	private AntPathMatcher matcher = new AntPathMatcher();

	@Override
	public List<TypeOpinion> resolve(InfoDetectionContext ctx) {
		
		List<TypeOpinion> opinions = null;
		String infoType = null;
		Information info = ctx.getInformation();
		
		if (info instanceof LinkAwareInfo) {
		
			String infoUrl = ((LinkAwareInfo) info).infoLink();
			
			if (StringUtil.hasText(infoUrl)) {
				
				infoUrl = infoUrl.toLowerCase();
				InfoDetectionConfiguration config = ctx.getInfoDetectionConfig();
				
				if (config != null) {
				
					Map<String, String> urlPatternTypes = config.getLinkPatternToContQId();
					
					if (CollectionUtil.isNotEmpty(urlPatternTypes)) {
						
						for (Map.Entry<String, String> entry : urlPatternTypes.entrySet()) {
							
							if (matcher.match(entry.getKey(), infoUrl)) {
								infoType = entry.getValue();
								break;
							}
						}
					}
				}
			}
		}
		
		if (StringUtil.hasText(infoType)) {
			
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			ContainerType containerDef = template.getContainerDefinition(infoType);
			
			if (containerDef != null) {
				opinions = Collections.singletonList(new TypeOpinion(infoType, name(), 1));
			}
		}
		
		return opinions;
	}

	@Override
	public String name() {
		return "Info Link Pattern Based Type resolver";
	}

	
	public static void main(String[] args) {
		String blogUrl = "https://www.eoriginal.com/bLOg/some-blog-post";
		String blogPattern = "**/blog/*";
		AntPathMatcher pattern = new AntPathMatcher();
		System.out.println(pattern.match(blogPattern, blogUrl.toLowerCase()));
	}
}

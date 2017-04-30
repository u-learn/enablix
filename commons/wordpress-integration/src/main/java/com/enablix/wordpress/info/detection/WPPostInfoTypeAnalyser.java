package com.enablix.wordpress.info.detection;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.AnalysisLevel;
import com.enablix.analytics.info.detection.InfoAnalyser;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.Opinion;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.config.Configuration;
import com.enablix.wordpress.integration.WordpressConstants;

@Component
public class WPPostInfoTypeAnalyser implements InfoAnalyser {

	private static final String BLOG_CONTAINER_Q_ID = "blog_containerQId";
	
	@Autowired
	private TemplateManager templateManager;

	@Override
	public List<Opinion> analyse(Information info, InfoDetectionContext ctx) {
		
		List<Opinion> opinions = null;
		
		Configuration config = ConfigurationUtil.getConfig(WordpressConstants.WP_POST_ANALYSER_TYPE_MAPPING);
		
		if (config != null) {
		
			String blogContainerQId = config.getStringValue(BLOG_CONTAINER_Q_ID);
			
			if (StringUtil.hasText(blogContainerQId)) {
			
				TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
				ContainerType containerDef = template.getContainerDefinition(blogContainerQId);
				
				if (containerDef != null) {
					opinions = new ArrayList<>();
					TypeOpinion typeOpinion = new TypeOpinion(blogContainerQId, name(), confidence());
					opinions.add(typeOpinion);
				}
			}
		}
		
		return opinions;
	}

	@Override
	public String name() {
		return WordpressConstants.WP_POST_TYPE_ANALYSER;
	}
	
	private float confidence() {
		return 1;
	}

	@Override
	public AnalysisLevel level() {
		return AnalysisLevel.L0;
	}

}

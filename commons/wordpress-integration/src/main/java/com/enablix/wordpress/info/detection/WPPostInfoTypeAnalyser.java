package com.enablix.wordpress.info.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.InfoTypeResolver;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.wordpress.integration.WPIntegrationProperties;
import com.enablix.wordpress.integration.WordpressConstants;
import com.enablix.wordpress.model.WordpressInfo;

public class WPPostInfoTypeAnalyser implements InfoTypeResolver {

	@Autowired
	private TemplateManager templateManager;

	@Override
	public List<TypeOpinion> resolve(InfoDetectionContext ctx) {
		
		List<TypeOpinion> opinions = null;
		Information information = ctx.getInformation();
		
		if (information instanceof WordpressInfo) {
			
			WordpressInfo wpInfo = (WordpressInfo) information;
			
			WPIntegrationProperties wpIntProps = WPIntegrationProperties.getFromConfiguration();
			
			if (wpIntProps != null) {
			
				String typeQId = wpIntProps.getDefaultContentTypeQId();
				float confidence = 0.5f;

				Map<String, String> wpCatToContQId = wpIntProps.getWpCatToContQId();
				
				if (CollectionUtil.isNotEmpty(wpCatToContQId)) {
					
					List<Long> categoryIds = wpInfo.getPost().getCategoryIds();
					Map<String, Integer> typeQIdCategoryCnt = new HashMap<>();
					
					String maxCntQId = null;
					Integer maxCnt = 0;
					int totalMatchCnt = 0;

					for (Long catId : categoryIds) {
						
						String contQId = wpCatToContQId.get(String.valueOf(catId));
					
						if (StringUtil.hasText(contQId)) {
						
							Integer cnt = typeQIdCategoryCnt.get(contQId);
							if (cnt == null) {
								cnt = 0;
							}
							typeQIdCategoryCnt.put(contQId, ++cnt);
							++totalMatchCnt;
							
							if (cnt > maxCnt) {
								maxCnt = cnt;
								maxCntQId = contQId;
							}
						}
					}
					
					if (StringUtil.hasText(maxCntQId)) {
						typeQId = maxCntQId;
						confidence = ((float) maxCnt) / totalMatchCnt;
					}
					
				}
				
				if (StringUtil.hasText(typeQId)) {
				
					TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
					ContainerType containerDef = template.getContainerDefinition(typeQId);
					
					if (containerDef != null) {
						opinions = new ArrayList<>();
						TypeOpinion typeOpinion = new TypeOpinion(typeQId, name(), confidence);
						opinions.add(typeOpinion);
					}
				}
			}
		}
		
		return opinions;
	}
	
	@Override
	public String name() {
		return WordpressConstants.WP_POST_TYPE_ANALYSER;
	}
	
}

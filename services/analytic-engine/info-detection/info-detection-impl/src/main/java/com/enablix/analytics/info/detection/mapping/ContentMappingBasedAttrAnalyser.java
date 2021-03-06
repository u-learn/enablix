package com.enablix.analytics.info.detection.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.info.detection.AnalysisLevel;
import com.enablix.analytics.info.detection.BaseInfoAnalyser;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.Opinion;
import com.enablix.analytics.info.detection.TypeAttrOpinion;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.mapper.ContentMapper;
import com.enablix.content.mapper.ContentMapperRegistry;
import com.enablix.content.mapper.ContentSource;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;

public class ContentMappingBasedAttrAnalyser extends BaseInfoAnalyser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentMappingBasedAttrAnalyser.class);
	
	@Autowired
	private ContentMapperRegistry mapperRegistry;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private SimpleInfoMapper simpleMapper;
	
	@Override
	protected List<Opinion> analyseInfo(InfoDetectionContext ctx) {
		
		Information info = ctx.getInformation();
		List<Opinion> opinions = new ArrayList<>();
		
		String tenantId = ProcessContext.get().getTenantId();
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		Collection<TypeOpinion> typeOpinions = ctx.getAssessment().getTypeOpinions();
		
		Map<String, Object> data = info.infoData();
		
		for (TypeOpinion typeOp : typeOpinions) {
		
			ContentSource contentSource = new ContentSource(mapperId(info), tenantId);
			ExternalContent content = new ExternalContent(contentSource, typeOp.getContainerQId(), data);
			ContentMapper mapper = mapperRegistry.getMapper(content);
			
			EnablixContent enablixContent = null;
					
			if (mapper == null) {
				
				LOGGER.info("No mapper found for [{}], content [{}]. Trying simple mapper.", 
						content.getContentSource(), content.getContentQId());
			
				ContainerType container = template.getContainerDefinition(typeOp.getContainerQId());
				if (container != null) {
					enablixContent = simpleMapper.transformToEnablixContent(container, content, template);
				}
				
			} else {
				
				enablixContent = mapper.transformToEnablixContent(content, template);
				
			}
			
			if (enablixContent != null) {
				
				for (Map.Entry<String, Object> entry : enablixContent.getData().entrySet()) {
					
					TypeAttrOpinion typeAttrOp = new TypeAttrOpinion(typeOp.getContainerQId(), 
							entry.getKey(), entry.getValue(), name(), typeOp.getConfidence());
					
					opinions.add(typeAttrOp);
				}
			}
		}
		
		/*if (true) {
			throw new NullPointerException();
		}*/
		
		return opinions;
	}

	private String mapperId(Information info) {
		return info.source() + "-" + info.type();
	}
	
	@Override
	public String name() {
		return "Content Mapping Based Attribute Analyser";
	}

	@Override
	public AnalysisLevel level() {
		return AnalysisLevel.L1;
	}

}

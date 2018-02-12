package com.enablix.analytics.info.detection.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.info.detection.AnalysisLevel;
import com.enablix.analytics.info.detection.BaseInfoAnalyser;
import com.enablix.analytics.info.detection.DocAwareInfo;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.Opinion;
import com.enablix.analytics.info.detection.TypeAttrOpinion;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.DocAttachment;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.services.util.TemplateUtil;

public class DocAttrAnalyser extends BaseInfoAnalyser {

	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public String name() {
		return "Doc Attribute Analyser";
	}

	@Override
	public AnalysisLevel level() {
		return AnalysisLevel.L1;
	}

	@Override
	protected Collection<Opinion> analyseInfo(InfoDetectionContext ctx) {

		Information info = ctx.getInformation();
		List<Opinion> opinions = new ArrayList<>();

		if (info instanceof DocAwareInfo) {
			
			DocAwareInfo docAwareInfo = (DocAwareInfo) info;
			List<DocAttachment> attachments = docAwareInfo.attachments();
			
			if (!CollectionUtil.isEmpty(attachments)) {
				
				DocAttachment attachment = attachments.get(0);
				TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
				
				Collection<TypeOpinion> typeOpinions = ctx.getAssessment().getTypeOpinions();
				
				for (TypeOpinion typeOp : typeOpinions) {
					
					String containerQId = typeOp.getContainerQId();
					ContainerType container = template.getContainerDefinition(containerQId);
					
					if (container != null) {
						ContentItemType docItem = TemplateUtil.getDocItemType(container);
						opinions.add(new TypeAttrOpinion(containerQId, docItem.getId(), attachment, name(), 0.99f));
					}
					
				}
			}
		}
		
		return opinions;
	}

}

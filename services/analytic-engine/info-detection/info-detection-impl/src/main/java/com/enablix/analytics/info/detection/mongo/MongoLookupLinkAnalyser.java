package com.enablix.analytics.info.detection.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.AnalysisLevel;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.InfoDetectorHelper;
import com.enablix.analytics.info.detection.InfoDetectorHelper.ContainerAndCollection;
import com.enablix.analytics.info.detection.InfoDetectorHelper.LookupCollectionAndContainers;
import com.enablix.analytics.info.detection.InfoTag;
import com.enablix.analytics.info.detection.LinkOpinion;
import com.enablix.analytics.info.detection.Opinion;
import com.enablix.analytics.info.detection.TaggedInfo;
import com.enablix.analytics.info.detection.TaggedInfoAnalyser;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.ContentDataUtil;

@Component
public class MongoLookupLinkAnalyser extends TaggedInfoAnalyser {

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private GenericDao dao;
	
	@Override
	public String name() {
		return "Mongo search linked content analyser";
	}

	@Override
	public AnalysisLevel level() {
		return AnalysisLevel.L1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Collection<Opinion> analyseTaggedInfo(TaggedInfo info, InfoDetectionContext ctx) {
		
		Set<Opinion> opinions = new HashSet<>();
		
		List<InfoTag> tags = info.tags();
		
		if (CollectionUtil.isNotEmpty(tags)) {
			
			TemplateFacade templateFacade = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			
			LookupCollectionAndContainers searchInTypes = InfoDetectorHelper.getLinkedContentCollections(ctx.getAssessment(), templateFacade);
			Map<String, Pattern> regexMap = new HashMap<>();
			
			for (ContainerAndCollection entry : searchInTypes.getEntries()) {
				
				List<String> searchInFields = getSearchInFields(entry.getContainerQId(), templateFacade);
				
				if (CollectionUtil.isNotEmpty(searchInFields)) {
					
					for (InfoTag tag : tags) {
						
						Criteria searchCriteria = new Criteria();
						
						Pattern tagRegex = regexMap.get(tag.tag());
						if (tagRegex == null) {
							tagRegex = Pattern.compile("^" + tag.tag() + "$", Pattern.CASE_INSENSITIVE);
							regexMap.put(tag.tag(), tagRegex);
						}

						List<Criteria> fieldCriterias = new ArrayList<>();
						for (String fieldId : searchInFields) {
							fieldCriterias.add(Criteria.where(fieldId).regex(tagRegex));
						}
						
						searchCriteria = searchCriteria.orOperator(fieldCriterias.toArray(new Criteria[0]));
						
						List<Map> result = dao.findByCriteria(searchCriteria, 
								entry.getCollection(), Map.class, MongoDataView.ALL_DATA);
						
						for (Map record : result) {
							ContentDataRef linkedRecord = ContentDataUtil.contentDataToRef(record, templateFacade, entry.getContainerQId());
							opinions.add(new LinkOpinion(linkedRecord, name(), analyserConfidence()));
						}
						
					} 
					
				}
			}
		}
		
		return opinions;
	}
	
	private List<String> getSearchInFields(String containerQId, TemplateFacade templateFacade) {
		
		List<String> fieldIds = new ArrayList<>();
		
		ContainerType containerDef = templateFacade.getContainerDefinition(containerQId);
		
		if (containerDef != null) {
			for (ContentItemType contentItem : containerDef.getContentItem()) {
				if (contentItem.getType() == ContentItemClassType.TEXT) {
					fieldIds.add(contentItem.getId());
				}
			}
		}
		
		return fieldIds;
	}

	

	private float analyserConfidence() {
		return 1f;
	}

}

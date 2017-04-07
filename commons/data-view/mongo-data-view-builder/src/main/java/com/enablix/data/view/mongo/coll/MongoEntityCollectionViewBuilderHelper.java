package com.enablix.data.view.mongo.coll;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.stereotype.Component;

import com.enablix.app.content.filter.ContentFilterIdResolver;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.core.domain.segment.DataSegmentAttribute;
import com.enablix.core.domain.segment.DataSegmentAware;
import com.enablix.core.mongo.entity.mapping.MongoEntityDetailHolder;
import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.data.view.mongo.AttributeMatcher;

@Component
public class MongoEntityCollectionViewBuilderHelper implements CollectionViewBuilderHelper {

	@Autowired
	private MongoEntityDetailHolder mongoEntityDetails;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private ContentFilterIdResolver filterIdResolver;
	
	@Autowired
	private DataSegmentInfoAttributeMatcher attributeMatcher;
	
	@Override
	public boolean isCollectionVisible(String collectionName, DataSegment dataSegment) {
		MongoPersistentEntity<?> persistentEntity = mongoEntityDetails.getPersistentEntity(collectionName);
		return persistentEntity != null;
	}

	@Override
	public boolean canHandle(String collectionName) {
		MongoPersistentEntity<?> persistentEntity = mongoEntityDetails.getPersistentEntity(collectionName);
		return persistentEntity != null;
	}

	@Override
	public List<DataSegmentAttrFilter> createDataSegmentFilters(String collectionName, DataSegment dataSegment) {
		
		// 1. Iterate through the data segment attributes defined in the template
		// 2. Look for the corresponding data segment attribute value in data segment
		// 3. if found, resolve the filter id for each data segment attribute

		List<DataSegmentAttrFilter> filters = new ArrayList<>();
		
		MongoPersistentEntity<?> persistentEntity = mongoEntityDetails.getPersistentEntity(collectionName);
		
		if (persistentEntity != null && DataSegmentAware.class.isAssignableFrom(persistentEntity.getType())) {
		
			TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
			
			for (String dsAttrId : template.getDataSegmentAttrIds()) {
				
				ContentItemType dsAttrContentItem = template.getDataSegmentAttrDefinition(dsAttrId);
				String recordFilterId = filterIdResolver.resolveFilterAttributeId(dsAttrContentItem, template);
				
				DataSegmentAttribute dataSegmentAttr = dataSegment.findAttribute(dsAttrId);
				filters.add(new DataSegmentAttrFilter(dataSegmentAttr, recordFilterId));
			}
		}
		
		return filters;
	}

	@Override
	public AttributeMatcher<DataSegmentAware> attributeMatcher() {
		return attributeMatcher;
	}

}

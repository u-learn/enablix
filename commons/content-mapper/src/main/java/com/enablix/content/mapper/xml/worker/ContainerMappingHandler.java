package com.enablix.content.mapper.xml.worker;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.content.ContentParser;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContainerMappingType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContainerMappingHandler {
	
	@Autowired
	private ContentCrudService crudService;

	public ContentDataRecord getContainerRecord(ContentContainerMappingType contentMapping, 
			ContainerMappingType containerMapping, ExternalContent extContent, ContentTemplate template) {
		
		ContentDataRecord record = null;
		
		ContentItemMappingType itemMapping = containerMapping.getItemMapping();
		Object extItemValue = ContentParser.getSingleValue(extContent.getData(), itemMapping.getValue());
		
		if (extItemValue != null) {
			
			String containerQId = containerMapping.getQualifiedId();
			
			if (StringUtil.isEmpty(containerQId)) {
				String containerId = containerMapping.getId();
				String parentContainerQId = contentMapping.getQualifiedId();
				containerQId = QIdUtil.createQualifiedId(parentContainerQId, containerId);
			}
			
			ContainerType container = TemplateUtil.findContainer(template.getDataDefinition(), containerQId);
			if (TemplateUtil.isLinkedContainer(container)) {
				container = TemplateUtil.findContainer(template.getDataDefinition(), container.getLinkContainerQId());
			}
			
			String collectionName = TemplateUtil.resolveCollectionName(template, container.getQualifiedId());
			
			String containerItemId = itemMapping.getItemId();
			SearchFilter itemIdFilter = createFilter(extItemValue, containerItemId);
			
			List<Map<String, Object>> records = crudService.findAllRecordForCriteria(
								collectionName, itemIdFilter.toPredicate(new Criteria()), MongoDataView.ALL_DATA);
			
			if (CollectionUtil.isNotEmpty(records)) {
				Map<String, Object> recordData = records.get(0);
				record = new ContentDataRecord(template.getId(), extContent.getContentQId(), recordData);
			}
		}
		
		return record;
	}

	private SearchFilter createFilter(Object extItemValue, String containerItemId) {
		
		String strValue = null;
		
		if (extItemValue instanceof String) {
			strValue = (String) extItemValue;
		} else {
			strValue = String.valueOf(extItemValue);
		}
		
		StringFilter itemIdFilter = new StringFilter(containerItemId, strValue, ConditionOperator.EQ);
		return itemIdFilter;
	}
	
}

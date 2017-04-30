package com.enablix.content.mapper.xml.worker;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.content.ContentParser;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.content.mapper.xml.MappingWorker;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.view.MongoDataView;

@Component
public class ExistingRecordResolver implements MappingWorker {

	@Autowired
	private ContentCrudService crudService;
	
	@Override
	public float executionOrder() {
		return MappingWorkerExecOrder.EXIST_RECORD_RESOLVER;
	}

	@Override
	public void execute(ContentContainerMappingType containerMapping, 
			ExternalContent extContent, EnablixContent ebxContent, TemplateFacade template) {
		
		Object externalId = ContentParser.getSingleValue(
					extContent.getData(), containerMapping.getSourceId().getValue());
		
		if (externalId != null) {
			
			SearchFilter extIdFilter = createSourceIdFilter(externalId);
			String collectionName = template.getCollectionName(extContent.getContentQId());
			
			Map<String, Object> ebxData = ebxContent.getData();
			List<Map<String, Object>> existingRecords = crudService.findAllRecordForCriteria(
					collectionName, extIdFilter.toPredicate(new Criteria()), MongoDataView.ALL_DATA);
			
			if (CollectionUtil.isNotEmpty(existingRecords)) {
				Map<String, Object> existRecord = existingRecords.get(0);
				ebxData.putAll(existRecord);
			} else {
				ebxData.put(ContentDataConstants.EXTERNAL_SOURCE_ID_KEY, externalId);
			}
		}
	}
	
	private SearchFilter createSourceIdFilter(Object externalId) {
		String strValue = StringUtil.stringValue(externalId);
		StringFilter extIdFilter = new StringFilter(ContentDataConstants.EXTERNAL_SOURCE_ID_KEY, strValue, ConditionOperator.EQ);
		return extIdFilter;
	}
	
}

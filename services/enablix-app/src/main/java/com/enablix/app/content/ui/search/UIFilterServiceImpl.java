package com.enablix.app.content.ui.search;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.mongo.search.service.SearchRequest;
import com.enablix.core.mongo.search.service.SearchRequestTransformer;
import com.enablix.core.mongo.view.MongoDataViewOperations;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;

@Component
public class UIFilterServiceImpl implements UIFilterService {

	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private SearchRequestTransformer requestTx;
	
	@Autowired
	protected MongoTemplate mongoTemplate;
	
	@Override
	public Map<String, List<RefListItemCount>> findRecordCountByRefListItem(
			String containerQId, SearchRequest searchRequest, DataView userView) {

		Map<String, List<RefListItemCount>> result = new HashMap<>();
		
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		ContainerType containerDef = template.getContainerDefinition(containerQId);
		
		if (containerDef != null) {

			String collectionName = template.getCollectionName(containerQId);
			
			MongoDataViewOperations op = new MongoDataViewOperations(mongoTemplate, DataViewUtil.getMongoDataView(userView));
			Criteria queryCriteria = requestTx.buildQueryCriteria(searchRequest);
			queryCriteria = op.getViewScopedQueryCriteria(queryCriteria, collectionName);
			
			for (ContentItemType contentItem : containerDef.getContentItem()) {
				
				if (contentItem.getType() == ContentItemClassType.BOUNDED) {
					
					List<AggregationOperation> aggs = new ArrayList<>();
					
					if (queryCriteria != null) {
						aggs.add(match(queryCriteria));
					}
					
					aggs.add(unwind(contentItem.getId()));
					aggs.add(group(contentItem.getId() + "." + ContentDataConstants.BOUNDED_ID_ATTR).count().as("sum"));
					aggs.add(project(Fields.from(
									Fields.field("id", "$_id." + ContentDataConstants.BOUNDED_ID_ATTR),
									Fields.field("count", "$sum"))));
					
					Aggregation aggregation = newAggregation(aggs);
					
					AggregationResults<RefListItemCount> aggValues = 
							mongoTemplate.aggregate(aggregation, collectionName, RefListItemCount.class);
					
					result.put(contentItem.getId(), aggValues.getMappedResults());
				}
			}
			
		}

		return result;
	}

}

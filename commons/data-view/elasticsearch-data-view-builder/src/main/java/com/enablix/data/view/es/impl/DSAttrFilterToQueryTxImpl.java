package com.enablix.data.view.es.impl;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.domain.segment.DataSegmentAttribute;
import com.enablix.core.domain.segment.DataSegmentAttribute.Matching;
import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.data.view.es.DSAttrFilterToQueryTx;
import com.enablix.services.util.ContentDataUtil;

@Component
public class DSAttrFilterToQueryTxImpl implements DSAttrFilterToQueryTx {

	@Override
	public QueryBuilder buildQuery(List<DataSegmentAttrFilter> filters, String type) {

		QueryBuilder result = null;
		
		if (CollectionUtil.isNotEmpty(filters)) {
			
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
			boolQuery.filter(QueryBuilders.typeQuery(type));
			
			for (DataSegmentAttrFilter filter : filters) {
				
				String recordAttId = filter.getRecordAttributeId();
				DataSegmentAttribute dsAttr = filter.getDataSegmentAttribute();

				BoolQueryBuilder attrQuery = QueryBuilders.boolQuery();
				
				if (dsAttr.getMatching() == Matching.LENIENT) {
					attrQuery.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(recordAttId)));
				}
				
				Object segmentValue = dsAttr.getValue();
				
				if (segmentValue instanceof String) {
					
					attrQuery.should(QueryBuilders.matchQuery(recordAttId, segmentValue));
					
				} else {
					
					List<String> txSegmentValue = ContentDataUtil.checkAndConvertToIdOrIdentityCollection(segmentValue);
					
					if (CollectionUtil.isNotEmpty(txSegmentValue)) {
						
						if (txSegmentValue.size() > 1) {
							
							BoolQueryBuilder boolValQuery = QueryBuilders.boolQuery();
							for (String val : txSegmentValue) {
								boolValQuery.should(QueryBuilders.matchQuery(recordAttId, val));
								boolValQuery.minimumNumberShouldMatch(1);
							}
							
							attrQuery.should(boolValQuery);
							
						} else {
							attrQuery.should(QueryBuilders.matchQuery(recordAttId, txSegmentValue.get(0)));
						}
						
					}
				}
				
				attrQuery.minimumNumberShouldMatch(1);
				
				boolQuery.filter(attrQuery);
				
			}
	
			result = boolQuery;
		}
		
		return result;
	}

}

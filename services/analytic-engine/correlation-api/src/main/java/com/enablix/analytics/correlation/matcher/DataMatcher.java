package com.enablix.analytics.correlation.matcher;

import java.util.List;
import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.PathItemType;
import com.enablix.core.commons.xsdtopojo.RelatedItemType;

public interface DataMatcher {

	List<Map<String, Object>> findMatchingRecords(String matchItemQId, ContentTemplate template,
			FilterCriteriaType filterCriteria, MatchInputRecord matchInput);
	
	List<Map<String, Object>> findMatchingRecords(ContentTemplate template,
			RelatedItemType relatedItemDef, MatchInputRecord matchInput);

	List<Map<String, Object>> findMatchingRecords(ContentTemplate template, PathItemType pathItem,
			MatchInputRecord matchInput);
	
	
}

package com.enablix.analytics.correlation.matcher;

import java.util.List;
import java.util.Map;

import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.PathItemType;
import com.enablix.core.commons.xsdtopojo.RelatedItemType;
import com.enablix.services.util.template.TemplateWrapper;

public interface DataMatcher {

	List<Map<String, Object>> findMatchingRecords(String matchItemQId, TemplateWrapper template,
			FilterCriteriaType filterCriteria, MatchInputRecord matchInput);
	
	List<Map<String, Object>> findMatchingRecords(TemplateWrapper template,
			RelatedItemType relatedItemDef, MatchInputRecord matchInput);

	List<Map<String, Object>> findMatchingRecords(TemplateWrapper template, PathItemType pathItem,
			MatchInputRecord matchInput);
	
	
}

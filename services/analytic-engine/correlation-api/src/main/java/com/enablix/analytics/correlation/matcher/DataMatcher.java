package com.enablix.analytics.correlation.matcher;

import java.util.List;
import java.util.Map;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.PathItemType;
import com.enablix.core.commons.xsdtopojo.RelatedItemType;

public interface DataMatcher {

	List<Map<String, Object>> findMatchingRecords(String matchItemQId, TemplateFacade template,
			FilterCriteriaType filterCriteria, MatchInputRecord matchInput);
	
	List<Map<String, Object>> findMatchingRecords(TemplateFacade template,
			RelatedItemType relatedItemDef, MatchInputRecord matchInput);

	List<Map<String, Object>> findMatchingRecords(TemplateFacade template, PathItemType pathItem,
			MatchInputRecord matchInput);
	
	
}

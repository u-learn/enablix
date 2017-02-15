package com.enablix.analytics.correlation.matcher;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class ParentAttributeFilterValueResolver implements FilterValueResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParentAttributeFilterValueResolver.class);
	
	@Override
	public Object resolve(FilterType filter, MatchInputRecord matchInput, TemplateWrapper template) {
		
		Map<String, Object> parentRecord = matchInput.getRecord();
		String filterAttrId = filter.getParentAttribute().getValue();
		
		String parentQId = filter.getParentAttribute().getParentQId();
		if (!StringUtil.isEmpty(parentQId)) {
			LOGGER.debug("Finding parent record with qualified id: {}", parentQId);
			parentRecord = findParentRecord(matchInput, parentQId);
		}
		
		if (parentRecord == null) {
			LOGGER.error("No parent record found for parentQId: {}", parentQId);
			return null;
		}
		
		Object attrValue = parentRecord.get(filterAttrId);
		return attrValue;
	}
	
	private Map<String, Object> findParentRecord(MatchInputRecord matchInput, String parentQId) {
		
		if (matchInput == null) {
			return null;
		}
		
		if (matchInput.getContentQId().equals(parentQId)) {
			return matchInput.getRecord(); 
		}
		
		return findParentRecord(matchInput.getParent(), parentQId);
		
	}

	@Override
	public boolean canHandle(FilterType filter) {
		return filter.getParentAttribute() != null;
	}

}

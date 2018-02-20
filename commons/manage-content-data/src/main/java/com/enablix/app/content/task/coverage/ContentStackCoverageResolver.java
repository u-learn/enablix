package com.enablix.app.content.task.coverage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentItemType;

@Component
public class ContentStackCoverageResolver {

	public Map<String, Long> getContentStats(ContentItemType contentItemDef, Map<String, Object> record) {
		
		Map<String, Long> stackStats = new HashMap<String, Long>();
		
		Object contentStack = record.get(contentItemDef.getId());
		
		if (contentStack instanceof Collection) {
		
			Collection<?> stack = (Collection<?>) contentStack;
			
			stack.forEach((stackItem) -> {
			
				if (stackItem instanceof Map<?,?>) {
				
					Map<?,?> stackItemProps = (Map<?,?>) stackItem;
					
					String stackItemQId = String.valueOf(stackItemProps.get("qualifiedId"));
					String itemQId = contentItemDef.getQualifiedId() + "." + stackItemQId;
					Long count = stackStats.get(itemQId);
					
					if (count == null) {
						count = 1L;
					} else {
						count++;
					}
					
					stackStats.put(itemQId, count);
				}
				
			});
		}
		
		return stackStats;
	}

}

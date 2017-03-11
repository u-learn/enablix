package com.enablix.app.content.task.coverage;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

@Component
public class ContentItemCoverageResolver {

	public int getCount(ContentItemType contentItemDef, Map<String, Object> record) {
		
		Object value = record.get(contentItemDef.getId());
		
		if (value == null || StringUtil.isStringAndEmpty(value) || CollectionUtil.isCollectionAndEmpty(value)) {
			return 0;
		}
		
		return 1;
	}

}

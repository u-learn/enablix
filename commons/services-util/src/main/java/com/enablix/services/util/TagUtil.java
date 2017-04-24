package com.enablix.services.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.Tag;

public class TagUtil {

	public static final Set<Tag> createTags(List<String> strTags) {
		
		return CollectionUtil.transform(strTags, 
			() -> new HashSet<Tag>(), 
			(in) -> new Tag(in));
		
	}
	
}

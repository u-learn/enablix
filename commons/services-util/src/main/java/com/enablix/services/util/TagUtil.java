package com.enablix.services.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.collection.CollectionUtil.CollectionCreator;
import com.enablix.commons.util.collection.CollectionUtil.ITransformer;
import com.enablix.core.api.Tag;

public class TagUtil {

	public static final Set<Tag> createTags(List<String> strTags) {
		
		return CollectionUtil.transform(strTags, 
			new CollectionCreator<Set<Tag>, Tag>() {
	
				@Override
				public Set<Tag> create() {
					return new HashSet<Tag>();
				}
				
			}, new ITransformer<String, Tag>() {
	
				@Override
				public Tag transform(String in) {
					return new Tag(in);
				}
				
			});
		
	}
	
}

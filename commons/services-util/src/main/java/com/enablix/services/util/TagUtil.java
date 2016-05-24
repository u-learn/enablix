package com.enablix.services.util;

import java.util.ArrayList;
import java.util.List;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.collection.CollectionUtil.CollectionCreator;
import com.enablix.commons.util.collection.CollectionUtil.ITransformer;
import com.enablix.core.api.Tag;

public class TagUtil {

	public static final List<Tag> createTags(List<String> strTags) {
		
		return CollectionUtil.transform(strTags, 
			new CollectionCreator<List<Tag>, Tag>() {
	
				@Override
				public List<Tag> create() {
					return new ArrayList<Tag>();
				}
				
			}, new ITransformer<String, Tag>() {
	
				@Override
				public Tag transform(String in) {
					return new Tag(in);
				}
				
			});
		
	}
	
}

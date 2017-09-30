package com.enablix.analytics.info.detection.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.enablix.analytics.info.detection.InfoDetectionConfiguration;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.InfoTag;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.TaggedInfo;
import com.enablix.commons.util.collection.CollectionUtil;

public class InfoTagAliasEnricher {

	public InfoDetectionContext enrich(InfoDetectionContext ctx) {
		
		Information info = ctx.getInformation();
		
		if (info instanceof TaggedInfo) {
		
			TaggedInfo taggedInfo = (TaggedInfo) info;
		
			InfoDetectionConfiguration config = ctx.getInfoDetectionConfig();
			
			Map<String, List<InfoTag>> tagAliases = config.getTagAliases();
			
			if (CollectionUtil.isNotEmpty(tagAliases)) {
				
				List<InfoTag> infoTags = taggedInfo.tags();
				
				if (CollectionUtil.isNotEmpty(infoTags)) {
					
					Set<InfoTag> aliasTags = new HashSet<>();
				
					infoTags.forEach((infoTag) -> {
						
						List<InfoTag> tagAliasList = tagAliases.get(infoTag.tag());
						
						if (CollectionUtil.isNotEmpty(tagAliasList)) {
							aliasTags.addAll(tagAliasList);
						}
					});
					
					taggedInfo.addTags(aliasTags);
				}
			}
		}
		
		return ctx;
	}
	
}

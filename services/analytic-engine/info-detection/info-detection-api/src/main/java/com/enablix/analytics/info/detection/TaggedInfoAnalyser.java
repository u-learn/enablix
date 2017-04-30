package com.enablix.analytics.info.detection;

import java.util.Collection;

public abstract class TaggedInfoAnalyser implements InfoAnalyser {

	@Override
	public Collection<Opinion> analyse(Information info, InfoDetectionContext ctx) {
		
		if (info instanceof TaggedInfo) {
			return analyseTaggedInfo((TaggedInfo) info, ctx);
		}
		
		return null;
	}
	
	protected abstract Collection<Opinion> analyseTaggedInfo(TaggedInfo info, InfoDetectionContext ctx);

}

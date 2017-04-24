package com.enablix.analytics.info.detection;

import java.util.List;

public abstract class TaggedInfoAnalyser implements InfoAnalyser {

	@Override
	public List<Opinion> analyse(Information info, InfoDetectionContext ctx) {
		
		if (info instanceof TaggedInfo) {
			return analyseTaggedInfo((TaggedInfo) info, ctx);
		}
		
		return null;
	}
	
	protected abstract List<Opinion> analyseTaggedInfo(TaggedInfo info, InfoDetectionContext ctx);

}

package com.enablix.analytics.info.detection;

import java.util.Collection;

public abstract class TaggedInfoAnalyser extends BaseInfoAnalyser {

	@Override
	protected Collection<Opinion> analyseInfo(InfoDetectionContext ctx) {
		
		Information info = ctx.getInformation();
		if (info instanceof TaggedInfo) {
			return analyseTaggedInfo((TaggedInfo) info, ctx);
		}
		
		return null;
	}
	
	protected abstract Collection<Opinion> analyseTaggedInfo(TaggedInfo info, InfoDetectionContext ctx);

}

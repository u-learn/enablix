package com.enablix.analytics.info.detection;

import java.util.Collection;

import com.enablix.commons.util.collection.CollectionUtil;

public abstract class BaseInfoAnalyser implements InfoAnalyser {

	@Override
	public InfoDetectionContext analyse(InfoDetectionContext ctx) {
			
		Collection<Opinion> opinions = analyseInfo(ctx);

		if (CollectionUtil.isNotEmpty(opinions)) {
			ctx.getAssessment().addOpinions(opinions);
		}
		
		return ctx;
	}
	
	protected abstract Collection<Opinion> analyseInfo(InfoDetectionContext ctx);

}

package com.enablix.analytics.info.detection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultInfoDetector implements InfoDetector {

	@Autowired
	private InfoDetectorSIGateway siGateway;
	
	@Override
	public List<ContentSuggestion> analyse(Information info) {
		InfoDetectionContext ctx = new InfoDetectionContext(info, new Assessment());
		ctx.setSaveContentRecord(false);
		return siGateway.analyse(ctx);
	}

	@Override
	public void analyseAndSaveContentRecord(Information info) {
		InfoDetectionContext ctx = new InfoDetectionContext(info, new Assessment());
		ctx.setSaveContentRecord(true);
		siGateway.analyse(ctx);
	}

}

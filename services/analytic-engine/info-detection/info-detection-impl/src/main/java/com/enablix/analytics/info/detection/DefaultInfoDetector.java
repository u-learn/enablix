package com.enablix.analytics.info.detection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;

@Component
public class DefaultInfoDetector implements InfoDetector {

	@Autowired
	private AnalyserRegistry registry;
	
	@Autowired
	private ContentSuggestionBuilder suggestionBuilder;
	
	@Override
	public List<ContentSuggestion> analyse(Information info) {
		
		Assessment assessment = new Assessment();
		InfoDetectionContext ctx = new InfoDetectionContext(assessment);
		
		for (InfoAnalyser analyser : registry.allAnalysers()) {
			
			List<Opinion> opinions = analyser.analyse(info, ctx);

			if (CollectionUtil.isNotEmpty(opinions)) {
				assessment.addOpinions(opinions);
			}
		}

		return suggestionBuilder.build(assessment);
	}

	
	
}

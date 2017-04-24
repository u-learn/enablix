package com.enablix.wordpress.info.detection;

import java.util.List;

import org.springframework.stereotype.Component;

import com.enablix.analytics.info.detection.AnalysisLevel;
import com.enablix.analytics.info.detection.InfoAnalyser;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.Opinion;
import com.enablix.wordpress.integration.WordpressConstants;

@Component
public class WPPostInfoTypeAnalyser implements InfoAnalyser {

	@Override
	public List<Opinion> analyse(Information info, InfoDetectionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		return WordpressConstants.WP_POST_TYPE_ANALYSER;
	}

	@Override
	public AnalysisLevel level() {
		return AnalysisLevel.L0;
	}

}

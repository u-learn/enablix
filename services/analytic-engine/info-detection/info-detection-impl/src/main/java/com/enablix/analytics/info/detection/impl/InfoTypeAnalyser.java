package com.enablix.analytics.info.detection.impl;

import java.util.ArrayList;
import java.util.List;

import com.enablix.analytics.info.detection.AnalysisLevel;
import com.enablix.analytics.info.detection.BaseInfoAnalyser;
import com.enablix.analytics.info.detection.InfoDetectionContext;
import com.enablix.analytics.info.detection.InfoTypeResolver;
import com.enablix.analytics.info.detection.Opinion;
import com.enablix.analytics.info.detection.TypeOpinion;
import com.enablix.commons.util.collection.CollectionUtil;

public class InfoTypeAnalyser extends BaseInfoAnalyser {

	private List<InfoTypeResolver> infoTypeResolvers;
	
	@Override
	protected List<Opinion> analyseInfo(InfoDetectionContext ctx) {
		
		List<Opinion> opinions = null;
		
		for (InfoTypeResolver resolver : infoTypeResolvers) {
			
			List<TypeOpinion> infoTypes = resolver.resolve(ctx);
			
			if (CollectionUtil.isNotEmpty(infoTypes)) {
				opinions = new ArrayList<>();
				opinions.addAll(infoTypes);
				break;
			}
		}
		
		return opinions;
	}
	
	public List<InfoTypeResolver> getInfoTypeResolvers() {
		return infoTypeResolvers;
	}

	public void setInfoTypeResolvers(List<InfoTypeResolver> infoTypeResolvers) {
		this.infoTypeResolvers = infoTypeResolvers;
	}

	@Override
	public String name() {
		return "Info Type Analyser";
	}
	
	@Override
	public AnalysisLevel level() {
		return AnalysisLevel.L0;
	}

}

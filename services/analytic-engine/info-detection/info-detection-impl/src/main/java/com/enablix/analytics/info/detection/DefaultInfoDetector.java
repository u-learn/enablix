package com.enablix.analytics.info.detection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultInfoDetector implements InfoDetector {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInfoDetector.class);
	
	@Autowired
	private InfoDetectorSIGateway siGateway;
	
	@Autowired
	private InfoDetectionConfigProviderFactory configProviderFactory;
	
	@Override
	public List<ContentSuggestion> analyse(Information info) {
		
		InfoDetectionConfiguration config = getInfoDetectionConfig(info);
		
		InfoDetectionContext ctx = new InfoDetectionContext(info, new Assessment(), config);
		
		ctx.setSaveContentRecord(false);
		return siGateway.analyse(ctx);
	}

	private InfoDetectionConfiguration getInfoDetectionConfig(Information info) {
		
		InfoDetectionConfiguration config = null;
		InfoDetectionConfigProvider configProvider = configProviderFactory.getConfigProvider(info.type());
		
		if (configProvider == null) {
			
			LOGGER.warn("No info detection configuration provider defined for info type [{}]", info.type());
			
		} else {
			config = configProvider.getConfiguration();
		}
		
		return config == null ? InfoDetectionConfiguration.NO_CONFIG : config;
	}

	@Override
	public void analyseAndSaveContentRecord(Information info) {
		
		InfoDetectionConfiguration config = getInfoDetectionConfig(info);

		InfoDetectionContext ctx = new InfoDetectionContext(info, new Assessment(), config);
		ctx.setSaveContentRecord(true);
		
		siGateway.analyse(ctx);
	}

}

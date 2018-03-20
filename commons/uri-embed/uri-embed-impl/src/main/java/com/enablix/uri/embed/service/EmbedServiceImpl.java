package com.enablix.uri.embed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.uri.embed.EmbedInfo;
import com.enablix.uri.embed.EmbedService;

@Component
public class EmbedServiceImpl implements EmbedService {

	@Autowired
	private LocalEmbedInfoCache localCache;
	
	@Autowired
	private EmbedInfoProvider provider;
	
	@Autowired
	private IFrameTester iframeTester;
	
	@Override
	public EmbedInfo getEmbedInfo(String url) {
		
		EmbedInfo embedInfo = localCache.get(url);
		
		if (embedInfo == null) {
			
			embedInfo = provider.fetchEmbedInfo(url);
			
			if (embedInfo != null) {
			
				if (embedInfo.isError()) {
					embedInfo = null;
				} else {
					localCache.put(url, embedInfo);
				}
			}
			
		} else {
			
			if (embedInfo.getIframeEmbeddable() == null) {
				embedInfo.setIframeEmbeddable(iframeTester.checkIFrameEmbeddable(url));
				localCache.put(url, embedInfo);
			}
		}
		
		return embedInfo;
	}
	
}

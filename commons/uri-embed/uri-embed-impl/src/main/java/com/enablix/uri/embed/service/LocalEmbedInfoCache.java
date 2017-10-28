package com.enablix.uri.embed.service;

import com.enablix.core.domain.uri.embed.EmbedInfo;

public interface LocalEmbedInfoCache {

	EmbedInfo get(String url);
	
	void put(String url, EmbedInfo embedInfo);
	
}

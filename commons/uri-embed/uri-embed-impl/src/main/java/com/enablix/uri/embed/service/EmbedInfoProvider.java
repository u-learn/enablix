package com.enablix.uri.embed.service;

import com.enablix.core.domain.uri.embed.EmbedInfo;

public interface EmbedInfoProvider {

	EmbedInfo fetchEmbedInfo(String url);
	
}

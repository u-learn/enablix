package com.enablix.uri.embed.service;

import com.enablix.core.domain.uri.embed.EmbedInfo;
import com.enablix.uri.embed.EmbedException;

public interface EmbedInfoProvider {

	EmbedInfo fetchEmbedInfo(String url) throws EmbedException;
	
}

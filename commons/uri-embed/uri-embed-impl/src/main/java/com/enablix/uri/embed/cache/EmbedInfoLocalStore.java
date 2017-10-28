package com.enablix.uri.embed.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.uri.embed.EmbedInfo;
import com.enablix.uri.embed.service.LocalEmbedInfoCache;

@Component
public class EmbedInfoLocalStore implements LocalEmbedInfoCache {

	@Autowired
	private EmbedInfoCrud crud;
	
	@Override
	public EmbedInfo get(String url) {
		return crud.getRepository().findByUrl(url);
	}

	@Override
	public void put(String url, EmbedInfo embedInfo) {
		crud.saveOrUpdate(embedInfo);
	}

}

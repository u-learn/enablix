package com.enablix.uri.embed.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.commons.util.beans.BeanUtil;
import com.enablix.core.domain.uri.embed.EmbedInfo;
import com.enablix.uri.embed.repo.EmbedInfoRepository;

@Component
public class EmbedInfoCrud extends MongoRepoCrudService<EmbedInfo> {

	@Autowired
	private EmbedInfoRepository repo;

	@Override
	public EmbedInfoRepository getRepository() {
		return repo;
	}

	@Override
	public EmbedInfo findExisting(EmbedInfo t) {
		return repo.findByUrl(t.getUrl());
	}
	
	@Override
	protected EmbedInfo merge(EmbedInfo t, EmbedInfo existing) {
		BeanUtil.copyBusinessAttributes(t, existing);
		return existing;
	}
	
}

package com.enablix.content.connection.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.content.connection.repo.ContentTypeConnectionRepository;
import com.enablix.core.domain.content.connection.ContentTypeConnection;

@Component
public class ContentConnectionCrudService extends MongoRepoCrudService<ContentTypeConnection> {

	@Autowired
	private ContentTypeConnectionRepository repo;
	
	@Override
	public ContentTypeConnectionRepository getRepository() {
		return repo;
	}

	@Override
	protected ContentTypeConnection merge(ContentTypeConnection t, ContentTypeConnection existing) {
		return t;
	}

}

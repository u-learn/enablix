package com.enablix.app.content.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.core.content.event.ContentDataDelEvent;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.mongo.repo.UserContentRelevanceRepository;

@Component
public class CleanUserContentRelevance implements ContentDataEventListener {

	@Autowired
	private UserContentRelevanceRepository repo;
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		// nothing to do
	}

	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		repo.deleteByContentIdentity(event.getContentIdentity());
	}

}

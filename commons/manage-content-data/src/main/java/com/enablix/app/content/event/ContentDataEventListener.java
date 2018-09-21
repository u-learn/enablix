package com.enablix.app.content.event;

import com.enablix.core.content.event.ContentDataArchiveEvent;
import com.enablix.core.content.event.ContentDataDelEvent;
import com.enablix.core.content.event.ContentDataSaveEvent;

public interface ContentDataEventListener {

	public void onContentDataSave(ContentDataSaveEvent event);
	
	public void onContentDataDelete(ContentDataDelEvent event);
	
	default public void onContentDataArchive(ContentDataArchiveEvent event) {
		// Do nothing
	}
	
}

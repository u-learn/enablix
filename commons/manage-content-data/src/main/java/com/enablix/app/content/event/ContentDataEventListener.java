package com.enablix.app.content.event;

public interface ContentDataEventListener {

	public void onContentDataSave(ContentDataSaveEvent event);
	
	public void onContentDataDelete(ContentDataDelEvent event);
	
}

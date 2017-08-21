package com.enablix.app.content.ui;

import java.util.List;

import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;

public interface DisplayableContentService {

	DisplayableContent getDisplayableContent(String contentQId, String contentIdentity, DataView view);
	
	List<DisplayableContent> getDisplayableContent(String contentQId, List<String> contentIdentities, DataView view);

	void postProcessDisplayableContent(DisplayableContent dispRecord, String sharedWithEmailId);
	
}

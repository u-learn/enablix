package com.enablix.app.content.ui.format;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.core.ui.ContentPreviewInfo;
import com.enablix.core.ui.ContentPreviewInfo.PreviewProperty;
import com.enablix.core.ui.DisplayableContent;

public interface PreviewBuilder {

	ContentPreviewInfo buildPreview(DisplayableContent displayRecord, DisplayContext ctx);
	
	boolean canHandle(DisplayableContent displayRecord);
	
	PreviewProperty buildsUsingPreviewProperty();
	
}

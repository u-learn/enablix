package com.enablix.app.content.ui.format;

import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.core.ui.ContentPreviewInfo;
import com.enablix.core.ui.ContentPreviewInfo.PreviewProperty;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.RichTextValue;
import com.enablix.core.ui.TextPreviewInfo;

@Component
public class TextPreviewBuilder implements PreviewBuilder {

	@Override
	public ContentPreviewInfo buildPreview(DisplayableContent displayRecord, DisplayContext ctx) {
		
		TextPreviewInfo previewInfo = new TextPreviewInfo();
		
		RichTextValue richText = displayRecord.getRichText();
		if (richText != null) {
			previewInfo.setText(richText.getRawValue());
		}
		
		return previewInfo;
	}

	@Override
	public boolean canHandle(DisplayableContent displayRecord) {
		return displayRecord.getRichText() != null;
	}

	@Override
	public PreviewProperty buildsUsingPreviewProperty() {
		return PreviewProperty.TEXT;
	}

}

package com.enablix.app.content.ui.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.core.domain.uri.embed.EmbedInfo;
import com.enablix.core.ui.ContentPreviewInfo;
import com.enablix.core.ui.ContentPreviewInfo.PreviewProperty;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.Hyperlink;
import com.enablix.core.ui.UrlPreviewInfo;
import com.enablix.uri.embed.EmbedException;
import com.enablix.uri.embed.EmbedService;

@Component
public class UrlPreviewBuilder implements PreviewBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlPreviewBuilder.class);
	
	@Autowired
	private EmbedService embedService;
	
	@Override
	public ContentPreviewInfo buildPreview(DisplayableContent displayRecord, DisplayContext ctx) {
		
		UrlPreviewInfo previewInfo = new UrlPreviewInfo();
		
		Hyperlink hyperlink = displayRecord.getHyperlink();
		
		if (hyperlink != null) {
		
			try {
				EmbedInfo embedInfo = embedService.getEmbedInfo(hyperlink.getHref());
				previewInfo.setEmbedInfo(embedInfo);
			} catch (EmbedException e) {
				LOGGER.error("Error getting embed info for [" + hyperlink.getHref() + "]", e);
			}
		}
		
		return previewInfo;
	}

	@Override
	public boolean canHandle(DisplayableContent displayRecord) {
		return displayRecord.getHyperlink() != null;
	}

	@Override
	public PreviewProperty buildsUsingPreviewProperty() {
		return PreviewProperty.URL;
	}
	
}

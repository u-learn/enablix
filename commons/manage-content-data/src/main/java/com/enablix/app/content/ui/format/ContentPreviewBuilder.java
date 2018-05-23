package com.enablix.app.content.ui.format;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.core.ui.ContentPreviewInfo;
import com.enablix.core.ui.DisplayableContent;

@Component
public class ContentPreviewBuilder {

	@Autowired
	private DocPreviewBuilder docPreviewBuilder;
	
	@Autowired
	private UrlPreviewBuilder urlPreviewBuilder;
	
	@Autowired
	private TextPreviewBuilder textPreviewBuilder;
	
	private List<PreviewBuilder> previewBuilders;
	
	@PostConstruct
	public void init() {
		this.previewBuilders = new ArrayList<>();
		this.previewBuilders.add(docPreviewBuilder);
		this.previewBuilders.add(urlPreviewBuilder);
		this.previewBuilders.add(textPreviewBuilder);
	}
	
	public ContentPreviewInfo buildPreview(DisplayableContent dispRecord, DisplayContext ctx) {
		
		ContentPreviewInfo previewInfo = null;
		
		for (PreviewBuilder builder : previewBuilders) {
			
			if (builder.canHandle(dispRecord)) {
				
				previewInfo = builder.buildPreview(dispRecord, ctx);
				
				if (previewInfo != null) {
					break;
				}
				
			}
		}
		
		return previewInfo;
	}

}

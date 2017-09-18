package com.enablix.app.content.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;

@Component
public class DisplayableContentServiceImpl implements DisplayableContentService {

	@Autowired
	private ContentDataManager contentDataMgr;

	@Autowired
	private TemplateManager templateMgr;

	@Autowired
	private DisplayableContentBuilder contentBuilder;
	
	@Autowired
	private DocUnsecureAccessUrlPopulator docUrlPopulator;
	
	@Autowired
	private TextLinkProcessor textLinkProcessor;
	
	@Override
	public DisplayableContent getDisplayableContent(String contentQId, String contentIdentity, DataView view) {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);

		Map<String, Object> record = contentDataMgr.getContentRecord(
				ContentDataRef.createContentRef(templateId, contentQId, contentIdentity, null), template, view);
		
		DisplayableContent displayableContent = null;

		if (record != null && !record.isEmpty()) {
			
			ContentDataRecord dataRecord = new ContentDataRecord(templateId, contentQId, record);
	
			DisplayContext ctx = new DisplayContext();
	
			displayableContent = contentBuilder.build(template, dataRecord, ctx);
		}
		
		return displayableContent;
	}

	@Override
	public List<DisplayableContent> getDisplayableContent(String contentQId, List<String> contentIdentities, DataView view) {
		
		List<DisplayableContent> contentList = new ArrayList<>();
		
		contentIdentities.forEach((identity) -> {
			DisplayableContent content = getDisplayableContent(contentQId, identity, view);
			if (content != null) {
				contentList.add(content);
			}
		});
		
		return contentList;
	}
	
	@Override
	public void postProcessDisplayableContent(DisplayableContent dispRecord, String sharedWithEmailId) {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		docUrlPopulator.populateUnsecureUrl(dispRecord, sharedWithEmailId);
		textLinkProcessor.process(dispRecord, template, sharedWithEmailId);
		
	}

}

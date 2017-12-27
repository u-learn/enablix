package com.enablix.app.content.ui.format;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.share.SharedContentUrlCreator;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.TextLinkifier;
import com.enablix.commons.util.TextLinkifier.LinkifiedOutput;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.ui.DisplayField;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.FieldValue;
import com.enablix.core.ui.Hyperlink;
import com.enablix.core.ui.TextValue;

@Component
public class TextLinkProcessor {

	@Autowired
	private SharedContentUrlCreator urlCreator;
	
	public void process(DisplayableContent content, TemplateFacade template, String sharedWithEmail) {
		
		EmailExtLinkDecorator linkDecorator = new EmailExtLinkDecorator(
				urlCreator, sharedWithEmail, content.getRecordIdentity());
		
		for (DisplayField<?> field : content.getFields()) {
			
			FieldValue fieldVal = field.getValue();
			
			if (fieldVal instanceof TextValue) {

				TextValue txtVal = (TextValue) fieldVal;
				String contentItemQId = QIdUtil.createQualifiedId(content.getContainerQId(), field.getId());
				
				LinkifiedOutput linkifiedText = TextLinkifier.linkifyText(txtVal.getRawValue(), contentItemQId, linkDecorator);
				txtVal.setValue(linkifiedText.getLinkifiedText());
				
				if (CollectionUtil.isNotEmpty(linkifiedText.getLinks())) {
					
					List<Hyperlink> links = new ArrayList<>();
					
					linkifiedText.getLinks().forEach((link) -> {
						Hyperlink hyperlink = new Hyperlink(link.getHref(), link.getTitle(), link.getRawText());
						links.add(hyperlink);
					});
					
					txtVal.setLinks(links);
				}
			}
		}
	}
	
}

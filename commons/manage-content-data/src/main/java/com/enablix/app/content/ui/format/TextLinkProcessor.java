package com.enablix.app.content.ui.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.share.SharedContentUrlCreator;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.TextLinkifier;
import com.enablix.core.ui.DisplayField;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.FieldValue;
import com.enablix.core.ui.TextValue;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class TextLinkProcessor {

	@Autowired
	private SharedContentUrlCreator urlCreator;
	
	public void process(DisplayableContent content, TemplateWrapper template, String sharedWithEmail) {
		
		EmailExtLinkDecorator linkDecorator = new EmailExtLinkDecorator(
				urlCreator, sharedWithEmail, content.getRecordIdentity());
		
		for (DisplayField<?> field : content.getFields()) {
			
			FieldValue fieldVal = field.getValue();
			
			if (fieldVal instanceof TextValue) {

				TextValue txtVal = (TextValue) fieldVal;
				String contentItemQId = QIdUtil.createQualifiedId(content.getContainerQId(), field.getId());
				
				String linkifiedText = TextLinkifier.linkifyText(txtVal.getRawValue(), contentItemQId, linkDecorator);
				txtVal.setValue(linkifiedText);
			}
		}
	}
	
}

package com.enablix.app.mail;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.ui.format.DisplayableContent;
import com.enablix.app.content.ui.format.DisplayableContentBuilder;
import com.enablix.app.mail.web.EmailData;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mail.velocity.ShareContentScenarioInputBuilder;
import com.enablix.core.mail.velocity.input.ShareContentVelocityInput;

@Component
public class ShareEmailServiceImpl implements ShareEmailService {

	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private DisplayableContentBuilder contentBuilder;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private ShareContentScenarioInputBuilder mailInputBuilder;
	
	@Autowired
	private DocUnsecureAccessUrlPopulator docUrlPopulator;
	
	@Override
	public boolean sendEmail(EmailData data) {
		
		String templateId = ProcessContext.get().getTemplateId();
		ContentTemplate template = templateMgr.getTemplate(templateId);
		
		Map<String, Object> record = contentDataMgr.getContentRecord(
					new ContentDataRef(templateId, data.getContainerQId(), 
							data.getContentIdentity()), template);
		
		ContentDataRecord dataRecord = new ContentDataRecord(templateId, data.getContainerQId(), record);
		
		DisplayableContent displayableContent = contentBuilder.build(template, dataRecord);
		docUrlPopulator.process(displayableContent, data.getEmailId());
		
		ShareContentVelocityInput mailInput = mailInputBuilder.build(data.getEmailId(), displayableContent);
		
		return mailService.sendHtmlEmail(mailInput, data.getEmailId(), "shareContent");
		
	}

}

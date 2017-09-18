package com.enablix.app.mail;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.DisplayableContentBuilder;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.mail.web.EmailData;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentShareActivity.ShareMedium;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mail.velocity.ShareContentScenarioInputBuilder;
import com.enablix.core.mail.velocity.input.ShareContentVelocityInput;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ActivityLogger;

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

	@Autowired
	private TextLinkProcessor textLinkProcessor;

	@Override
	public boolean sendEmail(EmailData data, DataView view) {

		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);

		Map<String, Object> record = contentDataMgr.getContentRecord(
				ContentDataRef.createContentRef(templateId, data.getContainerQId(), 
						data.getContentIdentity(), null), template, view);
		
		boolean emailSent = false;

		if (record != null && !record.isEmpty()) {
			
			ContentDataRecord dataRecord = new ContentDataRecord(templateId, data.getContainerQId(), record);
	
			DisplayContext ctx = new DisplayContext();
	
			DisplayableContent displayableContent = contentBuilder.build(template, dataRecord, ctx);
	
			docUrlPopulator.populateUnsecureUrl(displayableContent, data.getEmailId());
			textLinkProcessor.process(displayableContent, template, data.getEmailId());
	
			ShareContentVelocityInput mailInput = mailInputBuilder.build(
					data.getEmailId(), displayableContent, data.getEmailCustomContent(), view);
	
			emailSent = mailService.sendHtmlEmail(mailInput, data.getEmailId(), "shareContent");
	
			// Audit content sharing
			ActivityLogger.auditContentShare(templateId, displayableContent, data.getEmailId(),
					ShareMedium.WEB, Channel.EMAIL, mailInput.getIdentity(), displayableContent.getTitle());
		}
		
		return emailSent;

	}
}

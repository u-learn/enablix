package com.enablix.app.mail;

import java.util.ArrayList;
import java.util.List;
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
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
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
	
			List<String> emailIds = new ArrayList<>();
			
			if (StringUtil.hasText(data.getEmailId())) {
				emailIds.add(data.getEmailId());
			}
			
			if (CollectionUtil.isNotEmpty(data.getEmailIds())) {
				emailIds.addAll(data.getEmailIds());
			}
			
			for (String emailId : emailIds) {
				
				docUrlPopulator.populateUnsecureUrl(displayableContent, emailId);
				textLinkProcessor.process(displayableContent, template, emailId);
		
				ShareContentVelocityInput mailInput = mailInputBuilder.build(
						emailId, displayableContent, data.getEmailCustomContent(), view);
		
				emailSent = mailService.sendHtmlEmail(mailInput, emailId, "shareContent");
		
				// Audit content sharing
				ActivityLogger.auditContentShare(templateId, displayableContent, emailId,
						ShareMedium.WEB, Channel.EMAIL, mailInput.getIdentity(), 
						displayableContent.getTitle());
			}
		}
		
		return emailSent;

	}
}

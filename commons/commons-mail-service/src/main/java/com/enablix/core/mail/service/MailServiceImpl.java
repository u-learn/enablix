package com.enablix.core.mail.service;

import java.io.StringWriter;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enablix.commons.util.DefaultAESParameterProvider;
import com.enablix.commons.util.EncryptionUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.domain.config.TemplateConfiguration;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.entities.MailEvent;
import com.enablix.core.mail.entities.ShareEmailClientDtls;
import com.enablix.core.mail.utility.MailConstants;
import com.enablix.core.mail.utility.MailUtility;
import com.enablix.core.mail.velocity.NewUserScenarioInputBuilder;
import com.enablix.core.mongo.config.repo.EmailConfigRepo;
import com.enablix.core.mongo.config.repo.SMTPConfigRepo;
import com.enablix.core.mongo.config.repo.TemplateConfigRepo;
import com.enablix.core.mq.EventSubscription;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Value("${from.mail.address}")
	private String emailId;
	
	@Value("${from.mail.personalName:}")
	private String fromPersonalName;

	@Value("${smtp.default.username}")
	private String smtpUser;
	
	@Value("${from.mail.password}")
	private String password;

	@Value("${smtp.default.port}")
	private String port;

	@Value("${smtp.default.server}")
	private String server;
	
	@Value("${mail.use.tenant.server:false}")
	private boolean useTenantServer;
	
	@Value("${bcc.mail.address:}")
	private String bcc;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private EmailConfigRepo emailConfigRepo;
	
	@Autowired
	private SMTPConfigRepo smtpConfigRepo;
	
	@Autowired
	private TemplateConfigRepo templateConfigRepo;
	
	// need to move scenario builders into a factory
	@Autowired
	private NewUserScenarioInputBuilder newUserScenarioInputBuilder;
	
	private EmailConfiguration defaultEmailConfig;
	
	@Autowired
	private MailContentProcessorFactory mailContentProcessorFactory;
	
	@PostConstruct
	public void init() {
		defaultEmailConfig = new EmailConfiguration();
		defaultEmailConfig.setEmailId(emailId);
		defaultEmailConfig.setSmtpUsername(smtpUser);
		defaultEmailConfig.setPassword(EncryptionUtil.getAesDecryptedString(
										password, new DefaultAESParameterProvider()));
		defaultEmailConfig.setSmtp(server);
		defaultEmailConfig.setPort(port);
		
		if (StringUtil.isEmpty(fromPersonalName)) {
			defaultEmailConfig.setPersonalName(emailId);
		}
	}
	
	@EventSubscription(eventName = Events.SEND_EMAIL)
	public void handleEmailEvent(MailEvent mail) {
		if (mail != null) {
			sendHtmlEmail(mail.getMailTemplateInput(), mail.getToEmailId(), mail.getMailScenario());
		}
	}
	
	@Override
	public boolean sendHtmlEmail(Object objectTobeMerged, String emailid, String scenario) {
		String templateName = scenario + MailConstants.EMAIL_BODY_SUFFIX;
		String subjectTemplateName = scenario + MailConstants.EMAIL_SUBJECT_SUFFIX;
		return sendHtmlEmail(objectTobeMerged, emailid, scenario, templateName, subjectTemplateName);
	}

	@Override
	public boolean sendHtmlEmail(Object objectTobeMerged, String emailid, String scenario, String bodyTemplateName,
			String subjectTemplateName) {

		String bcc = this.bcc;
		String elementName = MailConstants.EMAIL_TEMPLATE_OBJECTNAME;
		switch (scenario) {
		case MailConstants.SCENARIO_SET_PASSWORD:
		case MailConstants.SCENARIO_RESET_PASSWORD:
		case MailConstants.SCENARIO_USER_SIGNUP:
		case MailConstants.SCENARIO_PASSWORD_CONFIRMATION:
			User user = (User) objectTobeMerged;
			objectTobeMerged = newUserScenarioInputBuilder.build(emailid,user.getPassword());
			break;
		default:
			break;
		}

		boolean mailSent = false;
		
		try {
			String htmlBody = generateTemplateMessage(objectTobeMerged, bodyTemplateName, elementName,
					MailConstants.BODY_TEMPLATE_PATH);
			String subject = generateTemplateMessage(objectTobeMerged, subjectTemplateName, elementName,
					MailConstants.SUBJECT_TEMPLATE_PATH);
			preprocessMailContent(objectTobeMerged, scenario);
			mailSent = MailUtility.sendEmail(emailid, subject, htmlBody, bcc, this.resolveEmailConfiguration());
			postprocessMailContent(objectTobeMerged, scenario);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return mailSent;
	}
	
	private void preprocessMailContent(Object mailContent, String mailScenarioName) {
		List<MailContentProcessor> processors = mailContentProcessorFactory.getProcessors(mailScenarioName);
		if (processors != null) {
			for (MailContentProcessor processor : processors) {
				processor.preSend(mailContent);
			}
		}
	}
	
	private void postprocessMailContent(Object mailContent, String mailScenarioName) {
		List<MailContentProcessor> processors = mailContentProcessorFactory.getProcessors(mailScenarioName);
		if (processors != null) {
			for (MailContentProcessor processor : processors) {
				processor.postSend(mailContent);
			}
		}
	}
	
	@Override
	public ShareEmailClientDtls getHtmlEmail(Object objectTobeMerged, String emailid, String scenario) {
		String templateName = scenario + MailConstants.EMAIL_BODY_SUFFIX;
		String subjectTemplateName = scenario + MailConstants.EMAIL_SUBJECT_SUFFIX;
		return getHtmlEmail(objectTobeMerged, emailid, scenario, templateName, subjectTemplateName);
	}
	
	@Override
	public ShareEmailClientDtls getHtmlEmail(Object objectTobeMerged, String emailid, String scenario, String bodyTemplateName,
			String subjectTemplateName) {
		ShareEmailClientDtls emailClientDtls=null;
		String elementName = MailConstants.EMAIL_TEMPLATE_OBJECTNAME;
		switch (scenario) {
		case MailConstants.SCENARIO_SET_PASSWORD:
		case MailConstants.SCENARIO_RESET_PASSWORD:
		case MailConstants.SCENARIO_PASSWORD_CONFIRMATION:
			objectTobeMerged = newUserScenarioInputBuilder.build(emailid);
			break;
		default:
			break;
		}
		try {
			String htmlBody = generateTemplateMessage(objectTobeMerged, bodyTemplateName, elementName,
					MailConstants.BODY_TEMPLATE_PATH);
			String subject = generateTemplateMessage(objectTobeMerged, subjectTemplateName, elementName,
					MailConstants.SUBJECT_TEMPLATE_PATH);
			preprocessMailContent(objectTobeMerged, scenario);
			emailClientDtls = new ShareEmailClientDtls(subject,htmlBody);
			postprocessMailContent(objectTobeMerged, scenario);
			
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return emailClientDtls;
	}
	
	private String generateTemplateMessage(Object objectTobeMerged, String templateName, String elementName, String path) {
		Template emailTemplate = velocityEngine.getTemplate(path + templateName);
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put(elementName, objectTobeMerged);
		velocityContext.put(MailConstants.EMAIL_TEMPLATE_STRINGUTIL, new StringUtil());
		StringWriter stringWriter = new StringWriter();
		emailTemplate.merge(velocityContext, stringWriter);
		return stringWriter.toString();
	}

	

	@Override
	public EmailConfiguration getEmailConfiguration() {

		EmailConfiguration emailConfig = null;
		
		List<EmailConfiguration> emailConfigs = emailConfigRepo.findAll();
		if (emailConfigs != null && !emailConfigs.isEmpty()) {
			emailConfig = emailConfigs.get(0);
		}			
		
		return emailConfig;
	}
	
	private EmailConfiguration resolveEmailConfiguration() {
		
		EmailConfiguration emailConfig = null;
		
		if (useTenantServer) {
			emailConfig = getEmailConfiguration();
		}
		
		if (emailConfig == null) {
			emailConfig = EmailConfiguration.createCopy(defaultEmailConfig);
		} else {
			EmailConfiguration.merge(defaultEmailConfig, emailConfig);
		}
		
		return emailConfig;
	}
	
	@Override
	public EmailConfiguration addEmailConfiguration(EmailConfiguration emailConfiguration) {
		return emailConfigRepo.save(emailConfiguration);
	}

	@Override
	public SMTPConfiguration getSMTPConfig(String domainName) {
		return smtpConfigRepo.findByDomainName(domainName);
	}

	@Override
	public Boolean deleteEmailConfiguration(EmailConfiguration emailConfiguration) {
		try {
			emailConfigRepo.delete(emailConfiguration);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public TemplateConfiguration getTemplateConfiguration(String scenario) {
		return templateConfigRepo.findByScenario(scenario);
	}

	@Override
	public TemplateConfiguration addTemplateConfiguration(TemplateConfiguration templateConfiguration) {
		return templateConfigRepo.save(templateConfiguration);
	}

	@Override
	public Boolean deleteTemplateConfiguration(TemplateConfiguration templateConfiguration) {
		try {
			templateConfigRepo.delete(templateConfiguration);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

};

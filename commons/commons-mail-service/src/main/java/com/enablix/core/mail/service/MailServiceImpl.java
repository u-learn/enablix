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
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.domain.config.TemplateConfiguration;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.mail.utility.MailConstants;
import com.enablix.core.mail.utility.MailUtility;
import com.enablix.core.mail.velocity.NewUserScenarioInputBuilder;
import com.enablix.core.mongo.config.repo.EmailConfigRepo;
import com.enablix.core.mongo.config.repo.SMTPConfigRepo;
import com.enablix.core.mongo.config.repo.TemplateConfigRepo;
import com.enablix.core.system.repo.TenantRepository;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Value("${from.mail.address}")
	private String emailId;

	@Value("${from.mail.password}")
	private String password;

	@Value("${smtp.default.port}")
	private String port;

	@Value("${smtp.default.server}")
	private String server;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private EmailConfigRepo emailConfigRepo;
	
	@Autowired
	private SMTPConfigRepo smtpConfigRepo;
	
	@Autowired
	private TemplateConfigRepo templateConfigRepo;
	
	@Autowired
	private TenantRepository tenantRepo;

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
		defaultEmailConfig.setPassword(EncryptionUtil.getAesDecryptedString(
										password, new DefaultAESParameterProvider()));
		defaultEmailConfig.setSmtp(server);
		defaultEmailConfig.setPort(port);
		defaultEmailConfig.setPersonalName(emailId);
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

		boolean mailSent = false;
		
		try {
			String htmlBody = generateTemplateMessage(objectTobeMerged, bodyTemplateName, elementName,
					MailConstants.BODY_TEMPLATE_PATH);
			String subject = generateTemplateMessage(objectTobeMerged, subjectTemplateName, elementName,
					MailConstants.SUBJECT_TEMPLATE_PATH);

			preprocessMailContent(objectTobeMerged, scenario);
			mailSent = MailUtility.sendEmail(emailid, subject, htmlBody, this.getEmailConfiguration());
			postprocessMailContent(objectTobeMerged, scenario);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return mailSent;
	};
	
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

	private String generateTemplateMessage(Object objectTobeMerged, String templateName, String elementName, String path) {
		Template emailTemplate = velocityEngine.getTemplate(path + templateName);
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put(elementName, objectTobeMerged);
		StringWriter stringWriter = new StringWriter();
		emailTemplate.merge(velocityContext, stringWriter);
		return stringWriter.toString();
	};

	

	@Override
	public EmailConfiguration getEmailConfiguration() {
		
		EmailConfiguration emailConfig = null;
		
		String tenantId = ProcessContext.get().getTenantId();
		String tenantName = null;
		
		if (!StringUtil.isEmpty(tenantId)) {
			
			Tenant tenant = tenantRepo.findByTenantId(tenantId);
			tenantName = tenant.getName();
			
			List<EmailConfiguration> emailConfigs = emailConfigRepo.findAll();
			if (emailConfigs != null && !emailConfigs.isEmpty()) {
				emailConfig = emailConfigs.get(0);
				if (StringUtil.isEmpty(emailConfig.getPersonalName())) {
					// for email configs already in the system
					emailConfig.setPersonalName(tenantName);
				}
			}
			
		}
		
		
		if (emailConfig == null) {
			// for default settings
			emailConfig = EmailConfiguration.createCopy(defaultEmailConfig);
			if (!StringUtil.isEmpty(tenantName)) {
				emailConfig.setPersonalName(tenantName);
			}
		}
		
		return emailConfig;
	};

	@Override
	public EmailConfiguration addEmailConfiguration(EmailConfiguration emailConfiguration) {
		Tenant tenant = tenantRepo.findByTenantId(ProcessContext.get().getTenantId());
		emailConfiguration.setPersonalName(tenant.getName());
		return emailConfigRepo.save(emailConfiguration);
	};

	@Override
	public SMTPConfiguration getSMTPConfig(String domainName) {
		return smtpConfigRepo.findByDomainName(domainName);
	};

	@Override
	public Boolean deleteEmailConfiguration(EmailConfiguration emailConfiguration) {
		try {
			emailConfigRepo.delete(emailConfiguration);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	};

	@Override
	public TemplateConfiguration getTemplateConfiguration(String scenario) {
		return templateConfigRepo.findByScenario(scenario);
	};

	@Override
	public TemplateConfiguration addTemplateConfiguration(TemplateConfiguration templateConfiguration) {
		return templateConfigRepo.save(templateConfiguration);
	};

	@Override
	public Boolean deleteTemplateConfiguration(TemplateConfiguration templateConfiguration) {
		try {
			templateConfigRepo.delete(templateConfiguration);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	};

};

package com.enablix.core.mail.service;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.domain.config.TemplateConfiguration;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.mail.utility.MailConstants;
import com.enablix.core.mail.utility.MailUtility;
import com.enablix.core.mail.velocity.NewUserScenarioInputBuilder;
import com.enablix.core.mail.velocity.ShareContentScenarioInputBuilder;
import com.enablix.core.mail.velocity.WeeklyDigestScenarioInputBuilder;
import com.enablix.core.mongo.config.repo.EmailConfigRepo;
import com.enablix.core.mongo.config.repo.SMTPConfigRepo;
import com.enablix.core.mongo.config.repo.TemplateConfigRepo;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.core.system.repo.UserRepository;


@Service
public class MailServiceImpl implements MailService {	
	
	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Autowired
	private VelocityEngine velocityEngine ;
	@Autowired
	private EmailConfigRepo emailConfigRepo;
	@Autowired
	private SMTPConfigRepo  smtpConfigRepo;    
    @Autowired    
    private TemplateConfigRepo templateConfigRepo;
    @Autowired
	private UserRepository userRepo;
    @Autowired
	private TenantRepository tenantRepo;
    @Autowired
    private NewUserScenarioInputBuilder newUserScenarioInputBuilder;
    @Autowired
    private WeeklyDigestScenarioInputBuilder weeklyDigestScenarioInputBuilder;
    @Autowired
    private ShareContentScenarioInputBuilder shareContentScenarioInputBuilder;
   
	
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    @Override
    public boolean sendHtmlEmail(Object objectTobeMerged, String emailid,String scenario) {
		String templateName = scenario + MailConstants.EMAIL_BODY_SUFFIX;
		String subjectTemplateName = scenario + MailConstants.EMAIL_SUBJECT_SUFFIX;
		String elementName = MailConstants.EMAIL_TEMPLATE_OBJECTNAME;
		switch (scenario) { 
		case MailConstants.SCENARIO_SET_PASSWORD:
		case MailConstants.SCENARIO_RESET_PASSWORD:
		case MailConstants.SCENARIO_PASSWORD_CONFIRMATION:
			objectTobeMerged = newUserScenarioInputBuilder.build(emailid);
			break;	
		case MailConstants.SCENARIO_SHARE_CONTENT:
			objectTobeMerged = shareContentScenarioInputBuilder.build(emailid, objectTobeMerged);
		default:
			break;
		}
			
       try{
    	String htmlBody = generateTemplateMessage(objectTobeMerged,templateName,elementName,MailConstants.BODY_TEMPLATE_PATH);
    	String subject = generateTemplateMessage(objectTobeMerged,subjectTemplateName,elementName,MailConstants.SUBJECT_TEMPLATE_PATH);
    	
    	return MailUtility.sendEmail(emailid, subject, htmlBody, this.getEmailConfiguration());
       }catch(Exception e){
    	   logger.error(e.getMessage(), e);
    	   return false;
       }
        
	};

	private String generateTemplateMessage(Object objectTobeMerged,String templateName, String elementName, String path) {
		Template emailTemplate = null;
		try{
        	emailTemplate = velocityEngine.getTemplate(ProcessContext.get().getTenantId() + path + templateName);
        }catch(ResourceNotFoundException ex){
        	if (emailTemplate==null)
        		emailTemplate = velocityEngine.getTemplate("default" + path + templateName);
        }
        
        VelocityContext velocityContext = new VelocityContext(); 
        velocityContext.put(elementName, objectTobeMerged);
        StringWriter stringWriter = new StringWriter();
        emailTemplate.merge(velocityContext, stringWriter);
        return stringWriter.toString();
    };
	
    
    @Value("${from.mail.address}")
    private String emailId;
    
    @Value("${from.mail.password}")
    private String password;
    
    @Value("${smtp.default.port}")
    private String port;
    
    @Value("${smtp.default.server}")
    private String server;
    
    
    @Override
	public EmailConfiguration getEmailConfiguration() {
		if (emailConfigRepo.count()==1)
			return emailConfigRepo.findAll().get(0);
		else{
			//for default settings
			Tenant tenant = tenantRepo.findByTenantId(ProcessContext.get().getTenantId());
			 EmailConfiguration emailConfiguration = new EmailConfiguration(emailId, password, server, port, tenant.getName());
			 emailConfigRepo.save(emailConfiguration);
			 return emailConfiguration;
			}
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

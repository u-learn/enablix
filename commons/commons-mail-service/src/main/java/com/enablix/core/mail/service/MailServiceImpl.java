package com.enablix.core.mail.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.domain.config.TemplateConfiguration;
import com.enablix.core.mail.utility.MailUtility;
import com.enablix.core.mongo.config.repo.EmailConfigRepo;
import com.enablix.core.mongo.config.repo.SMTPConfigRepo;
import com.enablix.core.mongo.config.repo.TemplateConfigRepo;
import com.enablix.core.system.repo.UserRepository;


@Service
public class MailServiceImpl implements MailService {	
	
	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	
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
    
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    @Override
    public boolean sendHtmlEmail(Object objectTobeMerged, String emailid,String scenario) {
		String toMailAddress = emailid;
		String templateName = scenario+"_body.vm";
		String elementName = "obj";
				
		if(scenario.equalsIgnoreCase("resetpassword"))
			objectTobeMerged = userRepo.findByUserId(emailid); //done to pick system generated password
		 
       try{
    	URL url = ((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs()[0];
    	URL url_new = new URL(url.toString().substring(0, (url.toString().lastIndexOf("enablix-app")+"enablix-app".length())) + "/ext-resources/config/properties/mail.properties");
   		Properties props = new Properties();
   		InputStream input = new FileInputStream(url_new.toString().substring(6));
   		props.load(input);    		
         
   		String subject = props.getProperty(scenario+"_subject");
   		String fromMailAddress = props.getProperty("from.mail.address");   		
   		String htmlBody = generateMessageBody(objectTobeMerged, templateName,  elementName);
   		
   		return MailUtility.sendEmail(fromMailAddress, toMailAddress, subject, htmlBody, this.getEmailConfiguration());
       }catch(Exception e){
    	   logger.error(e.getMessage(), e);
    	   return false;
       }
        
	};

	private String generateMessageBody(Object objectTobeMerged,String templateName, String elementName) {
        Template emailTemplate = velocityEngine.getTemplate("templates/"+templateName);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put(elementName, objectTobeMerged);
        StringWriter stringWriter = new StringWriter();
        emailTemplate.merge(velocityContext, stringWriter);
        return stringWriter.toString();
    };
	
    @Override
	public EmailConfiguration getEmailConfiguration() {
		if (emailConfigRepo.count()==1)    	
    	return emailConfigRepo.findAll().get(0);
		else
			return null;
	};

	@Override
	public EmailConfiguration addEmailConfiguration(EmailConfiguration emailConfiguration) {
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

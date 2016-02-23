package com.enablix.core.mail.service;

import java.io.StringWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.domain.config.TemplateConfiguration;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.constant.MailConstant;
import com.enablix.core.mail.utility.MailUtility;
import com.enablix.core.mongo.config.repo.EmailConfigRepo;
import com.enablix.core.mongo.config.repo.SMTPConfigRepo;
import com.enablix.core.mongo.config.repo.TemplateConfigRepo;
import com.enablix.core.system.repo.UserRepository;


@Service
public class MailServiceImpl implements MailService {	
	
	
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
    public boolean sendHtmlEmail(String emailid, String tenantId,String scenario) {
    //public boolean sendHtmlEmail(Object objectToBeMerged, String tenantId,String scenario) {
		String fromMailAddress = null;
		String subject = null;
		String templateName = null;
		String elementName = "obj";
		String toMailAddress = emailid;
		
		User user = userRepo.findByUserId(emailid);
    		
    	TemplateConfiguration templateConfiguration = this.getTemplateConfiguration(scenario);
    	templateName = templateConfiguration!=null?templateConfiguration.getTemplateFile():""; //a .vm file
    	
	    if(fromMailAddress==null) {
            fromMailAddress = MailConstant.FROM_MAIL_ADDRESS;
        };
        if(subject==null) {
            subject = MailConstant.DEFAULT_SUBJECT;
        };
        
        String htmlBody = generateMessageBody(user, templateName,  elementName);
		//return MailUtility.sendEmail(fromMailAddress,toMailAddress,subject,htmlBody,this.getEmailConfiguration(tenantId));
        return MailUtility.sendEmail(fromMailAddress, toMailAddress, subject, htmlBody, this.getEmailConfiguration(tenantId)); //TODO: null check for email config
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
	public EmailConfiguration getEmailConfiguration(String tenantId) {
		return emailConfigRepo.findByTenantId(tenantId);
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
		// TODO Auto-generated method stub
		try {
			emailConfigRepo.delete(emailConfiguration);
			return true;
		} catch (Exception e) {
			
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
		// TODO Auto-generated method stub
		try {
			templateConfigRepo.delete(templateConfiguration);
			return true;
		} catch (Exception e) {
			
			return false;
		}
	};

};

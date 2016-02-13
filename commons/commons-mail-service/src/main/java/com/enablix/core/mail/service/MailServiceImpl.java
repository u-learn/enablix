package com.enablix.core.mail.service;

import java.io.StringWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.domain.config.SMTPConfiguration;
import com.enablix.core.mail.constant.MailConstant;
import com.enablix.core.mail.utility.MailUtility;
import com.enablix.core.system.repo.EmailConfigRepo;
import com.enablix.core.system.repo.SMTPConfigRepo;


@Service
public class MailServiceImpl implements MailService {	
	
	
	private VelocityEngine velocityEngine ;
	@Autowired
	private EmailConfigRepo emailConfigRepo;
	@Autowired
	private SMTPConfigRepo  smtpConfigRepo;    
    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    @Override
	public boolean sendHtmlEmail(Object objectToBeMerged, String elementName, String templateName, String toMailAddress, String fromMailAddress, String subject,String tenantId) {
		
	    if(fromMailAddress==null) {
            fromMailAddress = MailConstant.FROM_MAIL_ADDRESS;
        };
        if(subject==null) {
            subject = MailConstant.DEFAULT_SUBJECT;
        };
        
        String htmlBody = generateMessageBody(objectToBeMerged, templateName,  elementName);
		//return MailUtility.sendEmail(fromMailAddress,toMailAddress,subject,htmlBody,this.getEmailConfiguration(tenantId));
        return MailUtility.sendEmail(fromMailAddress, toMailAddress, subject, htmlBody, this.getEmailConfiguration(tenantId));
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
		
};

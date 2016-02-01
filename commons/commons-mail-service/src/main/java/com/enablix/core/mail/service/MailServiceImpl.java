package com.enablix.core.mail.service;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enablix.core.domain.config.EmailConfiguration;
import com.enablix.core.mail.constant.MailConstant;
import com.enablix.core.mail.utility.MailUtility;
import com.enablix.core.system.repo.EmailConfigRepo;


@Service
public class MailServiceImpl implements MailService {

	
	private VelocityEngine velocityEngine ;
	@Autowired
	private EmailConfigRepo emailConfigRepo;
    
    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    @Override
	public boolean sendHtmlEmail(Object objectToBeMerged, String elementName, String templateName, String toMailAddress, String fromMailAddress, String subject,String tetantId) {
		
	    if(fromMailAddress==null)
        {
            fromMailAddress = MailConstant.FROM_MAIL_ADDRESS;
        }
        if(subject==null)
        {
            subject = MailConstant.DEFAULT_SUBJECT;
        }
        
        String htmlBody = generateMessageBody(objectToBeMerged, templateName,  elementName);
		//return MailUtility.sendEmail(fromMailAddress,toMailAddress,subject,htmlBody,this.getEmailConfiguration(tetantId));
        return MailUtility.sendEmail(fromMailAddress, toMailAddress, subject, htmlBody, this.getEmailConfiguration(tetantId));
	}

	private String generateMessageBody(Object objectTobeMerged,String templateName, String elementName)
    {
        Template emailTemplate = velocityEngine.getTemplate("templates/"+templateName);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put(elementName, objectTobeMerged);
        StringWriter stringWriter = new StringWriter();
        emailTemplate.merge(velocityContext, stringWriter);
        return stringWriter.toString();
    };
	
	public EmailConfiguration getEmailConfiguration(String tetantId)
	{
		return emailConfigRepo.findByIdentity(tetantId);	
	}

	public EmailConfiguration addEmailConfiguration(EmailConfiguration emailConfiguration) {
		return emailConfigRepo.save(emailConfiguration);
	}

	
}

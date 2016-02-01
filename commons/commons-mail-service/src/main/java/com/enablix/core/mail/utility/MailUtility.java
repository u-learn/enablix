package com.enablix.core.mail.utility;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.slf4j.LoggerFactory;

import com.enablix.core.domain.config.EmailConfiguration;



public class MailUtility {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MailUtility.class);

	public static boolean sendEmail(String sendFromEmail, String sendToEmail,String subject, String htmlBody,EmailConfiguration emailConfiguration) {
		
		logger.info("sending mail..");
		final String username = emailConfiguration.getEmailId();
        final String password = emailConfiguration.getPassword();
        
		 Properties props = new Properties();
	        props.put("mail.smtp.host", emailConfiguration.getSmtp());
	        props.put("mail.smtp.port",emailConfiguration.getPort());
	        props.put("mail.smtp.user",username);
	        props.put("password",password);
	        props.put("mail.smtp.auth","true");
	        props.put("mail.transport.protocol","smtp");
	        props.put("mail.smtp.starttls.enable", "true");	
	        
	        Session session = Session.getDefaultInstance(props, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {	                
	                return new PasswordAuthentication(username,password); 
	            }
	        });
 
        try {
            Message msg = new MimeMessage(session);
          
            msg.setFrom(new InternetAddress(sendFromEmail));
           
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
            msg.setSubject(subject);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            multipart.addBodyPart(htmlPart);
            msg.setContent(multipart);
            Transport.send(msg);
            return true;
        } catch (AddressException e) {
        	logger.error(e.getMessage(), e);
            return false;
        } catch (MessagingException e) {
        	logger.error(e.getMessage(), e);
            return false;
        }
    }
}

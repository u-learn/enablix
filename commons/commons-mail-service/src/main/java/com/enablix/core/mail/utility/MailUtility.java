package com.enablix.core.mail.utility;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.LoggerFactory;

import com.enablix.core.domain.config.EmailConfiguration;

public class MailUtility {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MailUtility.class);

	public static boolean sendEmail(String sendToEmail,String subject, String htmlBody,EmailConfiguration emailConfiguration) {
		try{
			logger.info("sending mail.. " + emailConfiguration);
			
			String username;
			String password;
			String smtpport;
			String smtpserver;
			
			if(emailConfiguration!=null){
				username = emailConfiguration.getEmailId();
				password = emailConfiguration.getPassword();
				smtpserver = emailConfiguration.getSmtp();
				smtpport = emailConfiguration.getPort();
			}
			else
			{
				URL url = ((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs()[0];
		    	URL url_new = new URL(url.toString().substring(0, (url.toString().lastIndexOf("enablix-app"))) + "config/properties/mail.properties");
		   		logger.debug( "url: " + url + " , url_new : "   + url_new);
		    	Properties props = new Properties();
		   		InputStream input = new FileInputStream(url_new.toString().substring(6));
		   		props.load(input); 
		   		
		   		username = props.getProperty(MailConstants.FROM_ADDRESS);
		   		password = props.getProperty(MailConstants.FROM_PASSWORD);
				smtpserver = props.getProperty(MailConstants.FROM_SMTP_SERVER);
				smtpport = props.getProperty(MailConstants.FROM_SMTP_PORT);		   		
			}
			
			Properties props = new Properties();
	        props.put("mail.smtp.host", smtpserver);
	        props.put("mail.smtp.port",smtpport);
	        props.put("mail.smtp.user",username);
	        props.put("password",password);
	        props.put("mail.smtp.auth","true");
	        props.put("mail.transport.protocol","smtp");
	        props.put("mail.smtp.starttls.enable", "true");	
	        
	        final String authUsername = username;
            final String authPassword = password;
            
	        Session session = Session.getInstance(props, new Authenticator() {
	            @Override
	             protected PasswordAuthentication getPasswordAuthentication() {	                
	                return new PasswordAuthentication(authUsername,authPassword); 
	            }
	        });
	        
	        
	        logger.debug("session details" + session + " properties: " + props);
            Message msg = new MimeMessage(session);
          
            msg.setFrom(new InternetAddress(username));
           
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
            msg.setSubject(subject);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");
            multipart.addBodyPart(htmlPart);
           logger.debug("To: " + msg.getAllRecipients()[0] + "From: " +  msg.getFrom()[0]);
            msg.setContent(multipart);
            Transport.send(msg);  
            return true;
        } catch (AddressException e) {
        	logger.error(e.getMessage(), e);
            return false;
        } catch (MessagingException e) {
        	logger.error(e.getMessage(), e);
            return false;
        }catch (NullPointerException e) {
        	logger.error(e.getMessage(), e);
            return false;        
    	}catch (Exception e){
    		logger.error(e.getMessage(), e);
            return false; 
    	}		
	}
}

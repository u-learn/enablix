package com.enablix.core.mail.receive;

import static com.enablix.core.mail.utility.MailConstants.MAIL_FILE_PROP;
import static com.enablix.core.mail.utility.MailConstants.MAIL_HOST_PROP;
import static com.enablix.core.mail.utility.MailConstants.MAIL_PASSWORD_PROP;
import static com.enablix.core.mail.utility.MailConstants.MAIL_PORT_PROP;
import static com.enablix.core.mail.utility.MailConstants.MAIL_PROTOCOL_PROP;
import static com.enablix.core.mail.utility.MailConstants.MAIL_USER_PROP;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.core.api.DocAttachment;

@Component
public class MailReader {

	
	private static Logger LOGGER = LoggerFactory.getLogger(MailReader.class);
	
	public void readMails(Properties javaMailProperties, SearchTerm searchTerm, 
			MailProcessor msgProcessor) throws MessagingException, IOException {
		
		String host = javaMailProperties.getProperty(MAIL_HOST_PROP);
		String protocol = javaMailProperties.getProperty(MAIL_PROTOCOL_PROP, "imaps");
		String port = javaMailProperties.getProperty(MAIL_PORT_PROP, "993");
		String file = javaMailProperties.getProperty(MAIL_FILE_PROP, "INBOX");
		String user = javaMailProperties.getProperty(MAIL_USER_PROP);
		String password = javaMailProperties.getProperty(MAIL_PASSWORD_PROP);
		
		URLName url = new URLName(protocol, host, Integer.parseInt(port), file, user, password);
		Session session = Session.getInstance(javaMailProperties, null);
		
		Store store = null;
		Folder folder = null;
		
		try {
			store = session.getStore(url);
			store.connect();
			
			LOGGER.debug("Reading mails from: email - {}, folder - {}", user, file);
			
			folder = store.getFolder(url);
			folder.open(Folder.READ_WRITE);
			
			Message[] msgs = folder.search(searchTerm);
			
			if (msgs != null) {
			
				for (Message msg : msgs) {
					if (msg.match(searchTerm)) {
						msgProcessor.process(msg);
					}
				}
			}
			
		} finally {
			if (store != null) { store.close(); }
			if (folder != null && folder.isOpen()) { folder.close(false); }
		}
		
	}
	
	public static void main(String[] args) throws MessagingException, ParseException, IOException {
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty(MAIL_HOST_PROP, "imap.gmail.com");
		mailProperties.setProperty(MAIL_PROTOCOL_PROP, "imaps");
		mailProperties.setProperty(MAIL_USER_PROP, "dluthra82@gmail.com");
		mailProperties.setProperty(MAIL_PASSWORD_PROP, "4getit#123");
		
		SearchTerm fromTerm = new FromTerm(new InternetAddress("dikshit_luthra@yahoo.com"));
		
		Date sentAfterDate = new Date(1518175631000L);
		ReceivedDateTerm sentAfter = new ReceivedDateTerm(ComparisonTerm.GT, sentAfterDate);
	
		SearchTerm searchTerm = new AndTerm(fromTerm, sentAfter);
		
		System.out.println(sentAfterDate);
		MailReader reader = new MailReader();
		reader.readMails(mailProperties, searchTerm, (msg) -> {
			
			String textContent = null;
			Object content = msg.getContent();
			
			if (content instanceof String) {
				
				textContent = (String) content;
				
			} else if (content instanceof MimeMultipart) {
				
				System.out.println(msg.getSubject() + "-" + msg.getReceivedDate());
				
				MimeMultipart multiPartContent = (MimeMultipart) content;

				int parts = multiPartContent.getCount();
				
				for (int i = 0; i < parts; i++) {
					
					MimeBodyPart bodyPart = (MimeBodyPart) multiPartContent.getBodyPart(i);
					
					if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
						
						DocAttachment attachment = new DocAttachment();
						attachment.setFilename(bodyPart.getFileName());
						attachment.setInputStream(bodyPart.getInputStream());
						attachment.setContentType(bodyPart.getContentType());
						
					} else {
					
						String contentType = bodyPart.getContentType().toLowerCase();
						
						if (textContent == null && 
								(contentType.startsWith("text/plain") 
									|| contentType.startsWith("text/html"))) {
							textContent = String.valueOf(bodyPart.getContent());
						}
					}
				}
			}
		});
	}
	
	
}

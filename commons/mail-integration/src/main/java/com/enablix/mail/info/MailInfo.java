package com.enablix.mail.info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.enablix.analytics.info.detection.DocAwareInfo;
import com.enablix.analytics.info.detection.EnablixContentTypeAware;
import com.enablix.analytics.info.detection.InfoTag;
import com.enablix.analytics.info.detection.Information;
import com.enablix.analytics.info.detection.SimpleInfoTag;
import com.enablix.analytics.info.detection.TaggedInfo;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.DocAttachment;
import com.enablix.core.mail.utility.MailConstants;

public class MailInfo implements Information, EnablixContentTypeAware, DocAwareInfo, TaggedInfo {

	private static final String TAGS_PREFIX = "[Tags]:";

	private Map<String, Object> infoData;
	
	private String enablixTypeName;
	
	private List<DocAttachment> attachments;
	
	private List<InfoTag> tags;
	
	public MailInfo() {
		this.infoData = new HashMap<>();
		this.tags = new ArrayList<>();
	}
	
	@Override
	public String source() {
		return "Email";
	}

	@Override
	public String type() {
		return MailConstants.MAIL_INFO_TYPE;
	}

	@Override
	public Map<String, Object> infoData() {
		return infoData;
	}
	
	@Override
	public String enablixTypeName() {
		return enablixTypeName;
	}
	
	@Override
	public List<DocAttachment> attachments() {
		return attachments;
	}

	@Override
	public List<InfoTag> tags() {
		return tags;
	}

	@Override
	public void addTags(Collection<InfoTag> infoTags) {
		this.tags.addAll(infoTags);
	}

	public static MailInfo build(Message msg, String mailboxUser) throws IOException, MessagingException {
		
		MailInfo mailInfo = new MailInfo();
		
		String textContent = null;
		Object content = msg.getContent();
		
		if (content instanceof String) {
			
			textContent = (String) content;
			
		} else if (content instanceof MimeMultipart) {
			
			MimeMultipart multiPartContent = (MimeMultipart) content;
			
			addAttachmentsFromMultipart(mailInfo, multiPartContent);
			
			textContent = getTextContentFromMultipart(multiPartContent);
			
		}
		
		parseAndAddTitleAndType(msg, mailInfo);
		String desc = parseAndAddTags(textContent, mailInfo);
		
		mailInfo.infoData.put("id", createMessageId(msg, mailboxUser));
		mailInfo.infoData.put("desc", desc);
		mailInfo.infoData.put("subject", msg.getSubject());
		
		return mailInfo;
	}
	
	private static void addAttachmentsFromMultipart(MailInfo mailInfo, 
			MimeMultipart multiPartContent) throws MessagingException, IOException {
		
		int parts = multiPartContent.getCount();
		
		for (int i = 0; i < parts; i++) {
			
			MimeBodyPart bodyPart = (MimeBodyPart) multiPartContent.getBodyPart(i);
			
			if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
				
				if (mailInfo.attachments == null) {
					mailInfo.attachments = new ArrayList<>();
				}
				
				DocAttachment attachment = new DocAttachment();
				attachment.setFilename(bodyPart.getFileName());
				attachment.setInputStream(bodyPart.getInputStream());
				
				String contentType = bodyPart.getContentType();
				contentType = contentType == null ? contentType : contentType.toLowerCase();
				attachment.setContentType(contentType);
				
				mailInfo.attachments.add(attachment);
				
			} else if (bodyPart.isMimeType("multipart/*")) {
				
				Object partContent = bodyPart.getContent();
				
				if (partContent instanceof MimeMultipart) {
					addAttachmentsFromMultipart(mailInfo, (MimeMultipart) partContent);
				}
			}
		}
	}
	
	private static String getTextContentFromMultipart(
			MimeMultipart multiPartContent) throws MessagingException, IOException {
		
		String textContent = null;
		
		int parts = multiPartContent.getCount();
		
		for (int i = 0; i < parts; i++) {
			
			MimeBodyPart bodyPart = (MimeBodyPart) multiPartContent.getBodyPart(i);
			
			if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
				
				if (textContent == null) {
					
					if (bodyPart.isMimeType("text/plain")) {

						Object partContent = bodyPart.getContent();
						textContent = String.valueOf(partContent);
						break;
						
					} else if (bodyPart.isMimeType("multipart/*")) {
						
						Object partContent = bodyPart.getContent();
						
						if (partContent instanceof MimeMultipart) {
							textContent = getTextContentFromMultipart((MimeMultipart) partContent);
							break;
						}
					}
				}
			}
		}
		
		return textContent;
	}
	
	private static String parseAndAddTags(String textContent, MailInfo mailInfo) {
		
		String desc = textContent;
		
		if (StringUtil.hasText(textContent)) {
			
			int indxOfTags = textContent.indexOf(TAGS_PREFIX);
			
			if (indxOfTags >= 0) {
				
				String tagsLine = textContent.substring(indxOfTags + TAGS_PREFIX.length());
				
				String[] tagsArr = tagsLine.split(";");
				
				for (String tag : tagsArr) {
					mailInfo.tags.add(new SimpleInfoTag(tag.trim()));
				}
				
				desc = textContent.substring(0, indxOfTags);
			}
		}
		
		return desc;
	}

	private static String createMessageId(Message msg, String mailboxUser) throws MessagingException {
		
		String from = null;
		
		Address[] froms = msg.getFrom();
		if (froms != null && froms.length != 0) {
			
			Address fromAddr = froms[0];
			
			if (fromAddr instanceof InternetAddress) {
				from = ((InternetAddress) fromAddr).getAddress();
			} else {
				from = fromAddr.toString();
			}
		}
		
		return mailboxUser + (from != null ? ("-" + from) : "") + "-" + msg.getReceivedDate().getTime();
	}

	private static void parseAndAddTitleAndType(Message message, MailInfo mailInfo) throws MessagingException {
		
		String subject = message.getSubject();
		String title = subject;
		
		if (StringUtil.hasText(subject)) {
		
			int indexOfColon = subject.indexOf(':');
			
			if (indexOfColon > 0) {
			
				String typeName = subject.substring(0, indexOfColon - 1).trim();
				title = subject.substring(indexOfColon + 1).trim();
				
				mailInfo.enablixTypeName = typeName;
			}
			
			mailInfo.infoData.put("title", title);
		}
		
	}

	

}

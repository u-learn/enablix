package com.enablix.slack.integration.utils;

import java.util.ArrayList;
import java.util.List;

import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.DocRef;
import com.enablix.slack.integration.entities.SlackAttachment;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AttachmentDecorator {

	public static String getDecoratedAttachment(DisplayableContent displayableContent, String fallbackText,
			String footerIcon, String color, String footerText) 
			throws JsonProcessingException{

		String heading = displayableContent.getContainerLabel() + " - " + displayableContent.getTitle();
		
		DocRef docRef = displayableContent.getDoc();
		
		String docName="";
		String docURL="";
		if(docRef!=null) {
			docName = docRef.getName();
			docURL = docRef.getAccessUrl();
		}
		
		SlackAttachment slackAttachment = new SlackAttachment.SlackAttachmentBuidler()
				.fallBack(fallbackText + " : " + displayableContent.getPortalUrl())
				.title(heading)
				.text(docName,docURL)
				.titleLink(displayableContent.getPortalUrl())
				.color(color)
				.footerText(footerText)
				.footerIcon(footerIcon)
				.fields(displayableContent.getFields())
				.build();
		
		List<Object> attachments = new ArrayList<Object>();
		attachments.add(slackAttachment);
		
		return JsonUtil.toJsonString(attachments);
	}
}

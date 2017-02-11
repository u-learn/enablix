package com.enablix.app.slack.utils;

import java.util.ArrayList;
import java.util.List;

import com.enablix.app.slack.entities.SlackAttachment;
import com.enablix.core.ui.DisplayableContent;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AttachmentDecorator {

	public static String getDecoratedAttachment(DisplayableContent displayableContent, String fallbackText,
			String FOOTER_ICON, String COLOR,String FOOTER_TEXT) 
			throws JsonProcessingException{
		String heading = displayableContent.getContainerLabel()+" - "+displayableContent.getTitle();
		SlackAttachment slackAttachment = new SlackAttachment.SlackAttachmentBuidler()
				.fallBack(fallbackText +" : "+ displayableContent.getPortalUrl()).title(heading)
				.text(displayableContent.getPortalUrl()).titleLink(displayableContent.getPortalUrl())
				.color(COLOR).footerText(FOOTER_TEXT).footerIcon(FOOTER_ICON).fields(displayableContent.getFields()).build();
		List<Object> attachments = new ArrayList<Object>();
		attachments.add(slackAttachment);
		return JSONConverter.getJSONArray(attachments);
	}
}
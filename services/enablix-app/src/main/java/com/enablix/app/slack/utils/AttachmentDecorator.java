package com.enablix.app.slack.utils;

import com.enablix.app.slack.entities.SlackAttachment;
import com.enablix.app.slack.entities.SlackAttachments;

public class AttachmentDecorator {

	
	public static SlackAttachments getDecoratedAttachment(String portalURL,String contentName,String fallbackText){
		SlackAttachments slackAttachments = new SlackAttachments();
		SlackAttachment slackAttachment = new SlackAttachment.SlackAttachmentBuidler()
				.fallback(String.join(" : ",fallbackText, portalURL)).title(contentName).text(portalURL).title_link(portalURL).build();
		slackAttachments.getAttachments().add(slackAttachment);
		return slackAttachments;
	}
}
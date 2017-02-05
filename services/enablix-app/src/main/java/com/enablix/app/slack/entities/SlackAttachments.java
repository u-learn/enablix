package com.enablix.app.slack.entities;

import java.util.ArrayList;
import java.util.List;

public class SlackAttachments {
	List<SlackAttachment> attachments;
	public SlackAttachments() {
		attachments= new ArrayList<>();
	}
	public List<SlackAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<SlackAttachment> attachments) {
		this.attachments = attachments;
	}
}

package com.enablix.app.slack.entities;

import org.springframework.beans.factory.annotation.Value;

import com.enablix.commons.constants.AppConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SlackAttachment {

	private final static String FOOTER=AppConstants.SLACK_ENABLIX_FOOTER;

	String text;

	@JsonProperty("fallback")
	String fallBack;

	String color;

	String title;

	@JsonProperty("title_link")
	String titleLink;

	@Value("${slack.attachment.footer.icon}")
	String FOOTER_ICON;	

	private SlackAttachment(String fallBack, String title, String titleLink) {
		this.fallBack=fallBack;
		this.title=title;
		this.titleLink=titleLink;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFallBack() {
		return fallBack;
	}

	public void setFallBack(String fallBack) {
		this.fallBack = fallBack;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleLink() {
		return titleLink;
	}

	public void setTitleLink(String titleLink) {
		this.titleLink = titleLink;
	}

	public String getFOOTER() {
		return FOOTER;
	}

	public static class SlackAttachmentBuidler {
		String fallBack;
		String title;
		String titleLink;
		String text;
		
		public SlackAttachmentBuidler fallBack(String fallBack)	{
			this.fallBack = fallBack;
			return this;
		}
		
		public SlackAttachmentBuidler text(String text)	{
			this.text = text;
			return this;
		}
		
		public SlackAttachmentBuidler title(String title) {
			this.title = title;
			return this;
		}
		
		public SlackAttachmentBuidler titleLink(String titleLink) {
			this.titleLink = titleLink;
			return this;
		}
		
		public SlackAttachment build() {
			return new SlackAttachment(fallBack,title,titleLink);
		}
	}
}
package com.enablix.app.slack.entities;

import org.springframework.beans.factory.annotation.Value;

import com.enablix.commons.constants.AppConstants;

public class SlackAttachment {
	String text;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	String fallback;
	String color;
	String title;
	String title_link;
	@Value("${slack.attachment.footer.icon}")
	String footer_icon;	
	private SlackAttachment(String fallback, String title, String title_link) {
		this.fallback=fallback;
		this.title=title;
		this.title_link=title_link;
	}
	final String footer=AppConstants.SLACK_ENABLIX_FOOTER;


	public String getFallback() {
		return fallback;
	}
	public void setFallback(String fallback) {
		this.fallback = fallback;
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
	public String getTitle_link() {
		return title_link;
	}
	public void setTitle_link(String title_link) {
		this.title_link = title_link;
	}
	public String getFooter_icon() {
		return footer_icon;
	}
	public void setFooter_icon(String footer_icon) {
		this.footer_icon = footer_icon;
	}
	public String getFooter() {
		return footer;
	}
	public static class SlackAttachmentBuidler{
		String fallback;
		String title;
		String title_link;
		String text;
		public SlackAttachmentBuidler fallback(String fallback)
		{
			this.fallback = fallback;
			return this;
		}
		public SlackAttachmentBuidler text(String text)
		{
			this.text = text;
			return this;
		}
		public SlackAttachmentBuidler title(String title)
		{
			this.title = title;
			return this;
		}
		public SlackAttachmentBuidler title_link(String title_link)
		{
			this.title_link = title_link;
			return this;
		}
		public SlackAttachment build()
	      {
	         return new SlackAttachment(fallback,title,title_link);
	      }
	}
}
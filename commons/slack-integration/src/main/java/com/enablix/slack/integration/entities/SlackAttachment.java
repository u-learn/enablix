package com.enablix.slack.integration.entities;

import java.util.LinkedList;
import java.util.List;

import com.enablix.core.ui.DisplayField;
import com.enablix.core.ui.TextValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SlackAttachment {

	@JsonProperty("footer")
	private String footerText;

	@JsonProperty("fallback")
	String fallBack;

	String color;

	String title;

	@JsonProperty("title_link")
	String titleLink;

	@JsonProperty("footer_icon")
	String footerIcon;

	List<SlackAttachmentFields> fields;
	
	private SlackAttachment(String fallBack, String title, String titleLink, String footerIcon, String color,
			String footerText,List<SlackAttachmentFields> fields) {

		this.fallBack=fallBack;
		this.title=title;
		this.titleLink=titleLink;
		this.footerIcon= footerIcon;
		this.color=color;
		this.footerText=footerText;
		this.fields=fields;
	}


	public String getFooterIcon() {
		return footerIcon;
	}

	public void setFooterIcon(String footerIcon) {
		this.footerIcon = footerIcon;
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

	public String getFooterText() {
		return footerText;
	}

	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}

	public List<SlackAttachmentFields> getFields() {
		return fields;
	}

	public void setFields(List<SlackAttachmentFields> fields) {
		this.fields = fields;
	}
	
	public static class SlackAttachmentBuidler {
		String fallBack;
		String title;
		String titleLink;
		String text;
		String footerIcon;
		String color;
		String footerText;
		List<DisplayField<?>> fields;
		public SlackAttachmentBuidler fallBack(String fallBack)	{
			this.fallBack = fallBack;
			return this;
		}
		public SlackAttachmentBuidler footerText(String footerText)
		{
			this.footerText=footerText;
			return this;
		}
		public SlackAttachmentBuidler footerIcon(String footerIcon)	{
			this.footerIcon = footerIcon;
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
		
		public SlackAttachmentBuidler color(String color) {
			this.color = color;
			return this;
		}
		
		public SlackAttachmentBuidler fields(List<DisplayField<?>> fields) {
			this.fields = fields;
			return this;
		}
		
		public SlackAttachment build() {
			// To be used later when we extend the Slack Attachment functionality
			/*List<SlackAttachmentFields> slackAttachLst = new LinkedList<SlackAttachmentFields>();
			SlackAttachmentFields slackAttcField;
			for(DisplayField<?> field:fields)
			{
				String value = processDisplayField(field);
				if(value!=null && !value.isEmpty()){
					slackAttcField = new SlackAttachmentFields(field.getLabel(), value);
					slackAttachLst.add(slackAttcField);
				}
			}*/
			return new SlackAttachment(fallBack,title,titleLink,footerIcon,color,footerText,null);
		}
		// To be used later when we extend the Slack Attachment functionality
		/*private String processDisplayField(DisplayField<?> field) {
			if(field.getValue() instanceof TextValue)
			{
				TextValue txtVal = (TextValue) field.getValue();
				return txtVal.getValue();
			}
			else{
				return null;	
			}

		}*/
	}
}
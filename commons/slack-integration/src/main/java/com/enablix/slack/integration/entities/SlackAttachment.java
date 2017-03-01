package com.enablix.slack.integration.entities;

import java.util.List;

import com.enablix.core.ui.DisplayField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SlackAttachment {

	@JsonProperty("footer")
	private String footerText;

	@JsonProperty("fallback")
	private String fallBack;

	private String color;

	private String text;
	
	private String title;

	@JsonProperty("title_link")
	private String titleLink;

	@JsonProperty("footer_icon")
	private String footerIcon;

	private List<SlackAttachmentFields> fields;

	private SlackAttachment(String text, String fallBack, String title, String titleLink, String footerIcon, String color,
			String footerText,List<SlackAttachmentFields> fields) {
		this.text=text;
		this.fallBack=fallBack;
		this.title=title;
		this.titleLink=titleLink;
		this.footerIcon= footerIcon;
		this.color=color;
		this.footerText=footerText;
		this.fields=fields;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
		private String fallBack;
		private String title;
		private String titleLink;
		private String text;
		private String footerIcon;
		private String color;
		private String footerText;
		private List<DisplayField<?>> fields;

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
		
		public SlackAttachmentBuidler text(String docName, String docURL)	{
			if(docName!="" && docURL!= "")
				this.text ="<"+docURL+"|"+docName+">";
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
			/*
			List<SlackAttachmentFields> slackAttachLst = new LinkedList<SlackAttachmentFields>();
			for (DisplayField<?> field : fields) {

				String value = processDisplayField(field);

				if (value != null && !value.isEmpty()){
					SlackAttachmentFields slackAttcField = new SlackAttachmentFields(field.getLabel(), value);
					slackAttachLst.add(slackAttcField);
				}
			}
			 */
			//List<SlackAttachmentFields> slackFieldAttachLst = new ArrayList<SlackAttachmentFields>();
			//slackFieldAttachLst.add(new SlackAttachmentFields(docName, docURL));
			SlackAttachment slackAttac = new SlackAttachment(text, fallBack, title, titleLink, footerIcon, color, footerText, null);
			return slackAttac;
		}

		// To be used later when we extend the Slack Attachment functionality
		/*
		private String processDisplayField(DisplayField<?> field) {

			if (field.getValue() instanceof TextValue) {
				TextValue txtVal = (TextValue) field.getValue();
				return txtVal.getValue();
			}

			return null;	
		}*/
	}
}

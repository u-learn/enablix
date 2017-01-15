package com.enablix.core.mail.entities;

public class ShareEmailClientDtls {
	private String subject;
	private String htmlBodyContent;
	public ShareEmailClientDtls(String _subject,String htnlBodyContent)
	{
		this.subject=_subject;
		this.htmlBodyContent=htnlBodyContent;
	}
	public String getHtmlBodyContent() {
		return htmlBodyContent;
	}
	public void setHtmlBodyContent(String htmlBodyContent) {
		this.htmlBodyContent = htmlBodyContent;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
}

package com.enablix.commons.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextLinkifier {

	private static String LINK_PATTERN = "((ftp|https?)://|(www\\.)|(mailto:)?[A-Za-z0-9._%+-]+@)\\S*[^\\s.;,(){}<>]";
	private static Pattern pattern = Pattern.compile(LINK_PATTERN, Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
	private static DefaultLinkDecorator defaultLinkDecorator = new DefaultLinkDecorator("Click Here");

	/**
	 * @param text
	 * @return
	 */
	public static String linkifyText(String text) {
		return linkifyText(text, null, defaultLinkDecorator);
	}
	
	public static String linkifyText(String text, String contentItemQId, LinkDecorator linkDecorator) {
		
		Matcher matcher = pattern.matcher(text);
		
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			String url = matcher.group();
			url = url.startsWith("www") ? "http://" + url : url;
			matcher.appendReplacement(sb, "<a href=\"" + linkDecorator.getHref(url, contentItemQId) 
											+ "\" target=\"_blank\">" + linkDecorator.getLinkText(url)
											+ "</a>");
		}
		
		String linkifiedText = sb.toString();
		return linkifiedText.isEmpty() ? text : linkifiedText;
	}
	
	public static void main(String[] args) {
		String url = "some text www.google.com another text http://www.google.com ending text";
		System.out.println(linkifyText(url));
	}
	
	public static interface LinkDecorator {
		String getLinkText(String url);
		String getHref(String url, String contentItemQId);
	}
	
	public static class DefaultLinkDecorator implements LinkDecorator {

		private String linkText;
		
		public DefaultLinkDecorator(String linkText) {
			super();
			this.linkText = linkText;
		}

		@Override
		public String getHref(String url, String contentItemQId) {
			return url;
		}

		@Override
		public String getLinkText(String url) {
			return StringUtil.isEmpty(linkText) ? url : linkText;
		}
		
	}
	
}

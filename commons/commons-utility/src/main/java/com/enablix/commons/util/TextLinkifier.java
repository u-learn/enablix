package com.enablix.commons.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextLinkifier {

	private static String LINK_PATTERN = "((ftp|https?)://|(www[1-9]*\\.)|(mailto:)?[A-Za-z0-9._%+-]+@)\\S*[^\\s.;,(){}<>]";
	private static Pattern pattern = Pattern.compile(LINK_PATTERN, Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
	
	private static String URL_PATTERN = "((https?)://|(www[1-9]*\\.)|(mailto:)?[A-Za-z0-9._%+-]+@)\\S*[^\\s.;,(){}<>]";
	private static Pattern urlpattern = Pattern.compile(URL_PATTERN, Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
	
	private static DefaultLinkDecorator defaultLinkDecorator = new DefaultLinkDecorator("Click Here");

	/**
	 * @param text
	 * @return
	 */
	public static LinkifiedOutput linkifyText(String text) {
		return linkifyText(text, null, defaultLinkDecorator);
	}
	
	public static Collection<String> parseUrls(String text) {
		Collection<String> urls = new HashSet<>();
		Matcher matcher = urlpattern.matcher(text);
		while (matcher.find()) {
			String url = matcher.group();
			urls.add(url);
		}
		return urls;
	}
	
	public static LinkifiedOutput linkifyText(String text, String contentItemQId, LinkDecorator linkDecorator) {
		
		LinkifiedOutput op = new LinkifiedOutput();
		
		Matcher matcher = pattern.matcher(text);
		
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			
			String url = matcher.group();
			String rawText = url;
			
			url = url.startsWith("www") ? "http://" + url : url;
			
			String href = linkDecorator.getHref(url, contentItemQId);
			String linkTitle = linkDecorator.getLinkText(url);
			
			matcher.appendReplacement(sb, "<a href=\"" + href 
											+ "\" target=\"_blank\">" + linkTitle
											+ "</a>");
			
			op.addLink(href, linkTitle, rawText);
		}
		
		String linkifiedText = sb.toString();
		op.setLinkifiedText(linkifiedText.isEmpty() ? text : linkifiedText);
		
		return op;
	}
	
	public static void main(String[] args) {
		String url = "some text www.google.com another text http://www.google.com ending text";
		System.out.println(linkifyText(url));
		System.out.println(parseUrls(url));
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
	
	public static class LinkifiedOutput {
		
		private String linkifiedText;
		
		private List<Link> links;

		public String getLinkifiedText() {
			return linkifiedText;
		}

		public void addLink(String href, String linkTitle, String text) {
			
			if (links == null) {
				links = new ArrayList<>();
			}
			
			links.add(new Link(href, text, linkTitle));
		}

		public void setLinkifiedText(String linkifiedText) {
			this.linkifiedText = linkifiedText;
		}

		public List<Link> getLinks() {
			return links;
		}

		public void setLinks(List<Link> links) {
			this.links = links;
		}
		
	}

	public static class Link {
		
		private String href;

		private String rawText;
		
		private String title;

		public Link(String href, String rawText, String title) {
			super();
			this.href = href;
			this.rawText = rawText;
			this.title = title;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public String getRawText() {
			return rawText;
		}

		public void setRawText(String rawText) {
			this.rawText = rawText;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
	}
	
}

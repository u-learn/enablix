package com.enablix.commons.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private static String LINK_PATTERN = "((ftp|https?)://|(www\\.)|(mailto:)?[A-Za-z0-9._%+-]+@)\\S*[^\\s.;,(){}<>]";
	private static Pattern pattern = Pattern.compile(LINK_PATTERN, Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.CASE_INSENSITIVE);
	
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	/**
	 * @param text
	 * @return
	 */
	public static String linkifyText(String text) {
		Matcher matcher = pattern.matcher(text);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			if (matcher.group(1).equals("www.")) {
				matcher.appendReplacement(sb, "<a href=\"http://$0\" target=\"_blank\">Click Here</a>");
			} else {
				matcher.appendReplacement(sb, "<a href=\"$0\" target=\"_blank\">Click Here</a>");
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String url = "some text www.google.com another text http://www.google.com ending text";
		System.out.println(linkifyText(url));
	}
	
}

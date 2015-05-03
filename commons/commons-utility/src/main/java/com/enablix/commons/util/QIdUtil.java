package com.enablix.commons.util;

import com.enablix.commons.constants.ContentDataConstants;

public class QIdUtil {

	public static String getParentQId(String elementQId) {
		int lastIndxOfDot = elementQId.lastIndexOf('.');
		if (lastIndxOfDot > 0) {
			return elementQId.substring(0, lastIndxOfDot);
		}
		return "";
	}
	
	public static String getElementId(String elementQId) {
		int lastIndxOfDot = elementQId.lastIndexOf('.');
		if (lastIndxOfDot > 0) {
			return elementQId.substring(lastIndxOfDot + 1, elementQId.length());
		}
		return elementQId;
	}
	
	public static String[] splitQId(String elementQId) {
		return elementQId.split("\\" + ContentDataConstants.QUALIFIED_ID_SEP);
	}

	public static String createQualifiedId(String parentQualifiedId, String childId) {
		return parentQualifiedId + ContentDataConstants.QUALIFIED_ID_SEP + childId;
	}
	
}

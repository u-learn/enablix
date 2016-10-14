package com.enablix.services.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

public class ContentItemToStringTransformer {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	
	public static String convert(Object item, ContentItemType itemType) {
		
		String strVal = null;
		
		switch (itemType.getType()) {
			case DOC:
				
				if (item instanceof Map) {
					Map<?,?> docMap = (Map<?,?>) item;
					strVal = (String) docMap.get(ContentDataConstants.DOC_NAME_ATTR);
				}
				
				break;
				
			case BOUNDED:
				
				boolean first = true;
				if (item instanceof Collection) {
					
					for (Object itemObj : (Collection<?>) item) {
						
						if (itemObj instanceof Map) {
							
							if (first) {
								first = false;
							} else {
								strVal += ", ";
							}
							
							strVal += ((Map<?,?>) itemObj).get(ContentDataConstants.BOUNDED_LABEL_ATTR);
						}
						
					}
				}
				
				break;
				
			case DATE_TIME:
				
				if (item instanceof Date) {
					strVal = dateFormat.format((Date) item);
				} else {
					strVal = String.valueOf(item);
				}

				break;
				
			default:
				strVal = String.valueOf(item);
		}
		
		return strVal;
	}
	
}

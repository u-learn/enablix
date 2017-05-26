package com.enablix.content.mapper.xml.worker;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

@Component
public class DateTimeItemValueBuilder implements ContentItemValueBuilder<Date> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeItemValueBuilder.class);
	
	@Override
	public Date buildValue(ContentItemType contentItem, ContentItemMappingType itemMapping, Object extValue) {
		
		Date dateValue = null;
		
		if (extValue instanceof List<?>) {
			List<?> listValue = (List<?>) extValue;
			extValue = listValue.get(0);
		}
		
		if (extValue instanceof Date) {
			
			dateValue = (Date) extValue;
			
		} else if (extValue instanceof String) {
			
			try {
				dateValue = new DateTime(extValue).toDate();
			} catch (Throwable t) {
				LOGGER.error("Error parsing date time value: " + extValue, t);
			}
		}
		
		return dateValue;
	}

	@Override
	public boolean canHandle(ContentItemType contentItem) {
		return contentItem.getType() == ContentItemClassType.DATE_TIME;
	}

	
	
}

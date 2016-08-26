package com.enablix.app.content.ui.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.TextValue;

@Component
public class DateToTextValueBuilder implements FieldValueBuilder<TextValue, String>{

	private static final Logger LOGGER = LoggerFactory.getLogger(DateToTextValueBuilder.class);
	
	private static final String OUT_DATE_FORMAT = "MM/dd/yyyy";
	private static final String IN_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	private SimpleDateFormat inDateFormatter = new SimpleDateFormat(IN_DATE_FORMAT);
	private SimpleDateFormat outDateFormatter = new SimpleDateFormat(OUT_DATE_FORMAT);
	
	@Override
	public TextValue build(ContentItemType fieldDef, String fieldValue, ContentTemplate template, DisplayContext ctx) {
		
		Date date = null;
		
		try {
			date = inDateFormatter.parse(fieldValue);
		} catch (ParseException e) {
			LOGGER.error("Error parsing date [{}]", fieldValue);
			LOGGER.error("Error: ", e);
		}
		
		return new TextValue(date == null ? "" : outDateFormatter.format(date));
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.DATE_TIME;
	}

}

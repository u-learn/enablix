package com.enablix.doc.converter.word;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocConverter;
import com.enablix.doc.converter.DocumentStream;

@Component
public class WordToImageConverter implements DocConverter {

	@Override
	public void convertAndWrite(InputStream is, DocumentFormat inFormat, DocumentStream os, DocumentFormat outFormat)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canConvert(DocumentFormat from, DocumentFormat to) {
		// TODO Auto-generated method stub
		return false;
	}

}

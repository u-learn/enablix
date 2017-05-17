package com.enablix.doc.converter.ppt;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.stereotype.Component;

import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocConverter;
import com.enablix.doc.converter.DocumentStream;

@Component
public class PPTXtoImageConverter extends PowerpointToImageConverter implements DocConverter {
	
	@Override
	public void convertAndWrite(InputStream is, DocumentFormat inFormat, DocumentStream os, DocumentFormat outFormat)
			throws IOException {
		convertAndWrite(is, inFormat, os, outFormat, "png", (i) -> new XMLSlideShow(i));
	}

	@Override
	public boolean canConvert(DocumentFormat from, DocumentFormat to) {
		return from == DocumentFormat.PPTX && to == DocumentFormat.PNG;
	}

}

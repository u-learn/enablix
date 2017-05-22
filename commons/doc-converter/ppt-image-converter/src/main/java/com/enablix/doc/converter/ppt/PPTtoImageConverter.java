package com.enablix.doc.converter.ppt;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.springframework.stereotype.Component;

import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocConverter;
import com.enablix.doc.converter.DocumentStream;

@Component
public class PPTtoImageConverter extends PowerpointToImageConverter implements DocConverter {

	@Override
	public boolean canConvert(DocumentFormat from, DocumentFormat to) {
		return from == DocumentFormat.PPT && to == DocumentFormat.PNG;
	}

	@Override
	public void convertAndWrite(InputStream is, DocumentFormat inFormat, DocumentStream os, DocumentFormat outFormat)
			throws IOException {
		convertAndWrite(is, inFormat, os, outFormat, "png", (i) -> new HSLFSlideShow(i));
	}

}

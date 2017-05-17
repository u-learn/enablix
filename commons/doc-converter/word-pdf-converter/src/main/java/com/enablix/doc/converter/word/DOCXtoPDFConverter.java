package com.enablix.doc.converter.word;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocConverter;
import com.enablix.doc.converter.DocumentStream;

import fr.opensagres.xdocreport.core.document.DocumentKind;

@Component
public class DOCXtoPDFConverter extends WordToPDFConverter implements DocConverter {

	@Override
	public void convertAndWrite(InputStream is, DocumentFormat inFormat, DocumentStream os, DocumentFormat outFormat)
			throws IOException {
		convertAndWrite(is, inFormat, os, outFormat, DocumentKind.DOCX);
	}

	@Override
	public boolean canConvert(DocumentFormat from, DocumentFormat to) {
		return from == DocumentFormat.DOCX && to == DocumentFormat.PDF;
	}

}

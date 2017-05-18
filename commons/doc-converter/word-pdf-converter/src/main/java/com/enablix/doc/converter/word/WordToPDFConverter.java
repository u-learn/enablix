package com.enablix.doc.converter.word;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocumentStream;

import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.docx.poi.itext.XWPF2PDFViaITextConverter;
import fr.opensagres.xdocreport.core.document.DocumentKind;

public class WordToPDFConverter {

	protected void convertAndWrite(InputStream is, DocumentFormat inFormat, 
			DocumentStream os, DocumentFormat outFormat, DocumentKind docKind) throws IOException {
		
		// 2) Prepare Pdf options
		Options options = Options.getFrom(docKind);

		// 3) Convert XWPFDocument to Pdf
		os.startDocumentWriting();
		
		try {
			
			OutputStream out = os.nextPageOutputStream();
			
			os.startPageWriting();
			
			XWPF2PDFViaITextConverter.getInstance().convert(is, out, options);
			
			os.endPageWriting(new HashMap<>());
			
		} catch (XDocConverterException e) {
			os.writingError(e);
			throw new IOException("Converter error - word to pdf", e);
			
		} catch (Exception e) {
			os.writingError(e);
			throw new IOException("Error converting word to pdf", e);
		}
		
		
		os.endDocumentWriting(new HashMap<>());
		
	}

}

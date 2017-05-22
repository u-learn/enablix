package com.enablix.doc.converter.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.docx.poi.itext.XWPF2PDFViaITextConverter;
import fr.opensagres.xdocreport.core.document.DocumentKind;

public class DocxToPdf {

	public static void main(String args[]) throws IOException, XDocConverterException {

		String outFile = "C:\\Users\\dluthra\\Downloads\\preview\\doc\\Doc2.pdf";
		String filePath = "C:\\Users\\dluthra\\Downloads\\Doc2.docx";
		//String imageFilePrefix = "Resume";
		FileInputStream is = new FileInputStream(filePath);

		// 2) Prepare Pdf options
		Options options = Options.getFrom(DocumentKind.DOCX);

		// 3) Convert XWPFDocument to Pdf
		OutputStream out = new FileOutputStream(new File(outFile));
		XWPF2PDFViaITextConverter.getInstance().convert(is, out, options);
	}
}
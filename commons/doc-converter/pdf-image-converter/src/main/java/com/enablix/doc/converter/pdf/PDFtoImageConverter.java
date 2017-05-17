package com.enablix.doc.converter.pdf;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;

import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocConverter;
import com.enablix.doc.converter.DocumentStream;

@Component
public class PDFtoImageConverter implements DocConverter {

	@Override
	public void convertAndWrite(InputStream is, DocumentFormat inFormat, DocumentStream os, DocumentFormat outFormat)
			throws IOException {
		
		PDDocument document = PDDocument.load(is);

        PDFRenderer pdfRenderer = new PDFRenderer(document);
        
        os.startDocumentWriting();
        
        for (int page = 0; page < document.getNumberOfPages(); ++page) {

            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            OutputStream out = os.nextPageOutputStream();
			
			try {
				
				os.startPageWriting();
				
				ImageIO.write(bim, "png", out);
				
				os.endPageWriting();
				
			} catch (Exception e) {
				os.writingError(e);
				throw e;
			}
			
        }

        os.endDocumentWriting();
	}

	@Override
	public boolean canConvert(DocumentFormat from, DocumentFormat to) {
		return from == DocumentFormat.PDF && to == DocumentFormat.PNG;
	}

}

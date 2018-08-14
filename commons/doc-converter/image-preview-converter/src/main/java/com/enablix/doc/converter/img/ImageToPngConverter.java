package com.enablix.doc.converter.img;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.IOUtil;
import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocConverter;
import com.enablix.doc.converter.DocumentStream;

@Component
public class ImageToPngConverter implements DocConverter {

	@Value("${doc.image.formats}")
	private Set<DocumentFormat> imageFormats;
	
	@Value("${doc.image.preview.max-width:1100}")
	private int maxWidth = 1100; //800px
	
	@Override
	public void convertAndWrite(InputStream is, DocumentFormat inFormat, DocumentStream os, DocumentFormat outFormat)
			throws IOException {
		
        os.startDocumentWriting();
        
        OutputStream out = os.nextPageOutputStream();
			
		try {
			
			os.startPageWriting();
			
			Map<String, Object> imgProperties = IOUtil.resizeImage(
					is, maxWidth, DocumentFormat.PNG.toString().toLowerCase(), out);
			
			os.endPageWriting(imgProperties);
			
		} catch (Exception e) {
			os.writingError(e);
			throw e;
		}
			
        os.endDocumentWriting(new HashMap<String, Object>());
		
	}

	@Override
	public boolean canConvert(DocumentFormat from, DocumentFormat to) {
		return imageFormats.contains(from) && to == DocumentFormat.PNG;
	}

}

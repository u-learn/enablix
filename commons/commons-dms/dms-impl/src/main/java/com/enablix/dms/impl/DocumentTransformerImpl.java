package com.enablix.dms.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocNameStrategy;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.commons.dms.api.DocumentTransformer;
import com.enablix.commons.dms.api.DocumentTxStrategy;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.DocumentFormat;
import com.enablix.core.api.IDocument;
import com.enablix.doc.converter.DocConverter;
import com.enablix.doc.converter.DocConverterFactory;
import com.enablix.doc.converter.DocFormatResolver;
import com.enablix.doc.converter.DocumentStream;

@Component
public class DocumentTransformerImpl implements DocumentTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTransformerImpl.class);
	
	@Autowired
	private DocConverterFactory converterFactor;
	
	@Autowired
	private DocFormatResolver formatResolver;
	
	@Override
	public <SDM extends DocumentMetadata, SD extends Document<SDM>> 
			List<DocInfo> convertAndSaveToDocStore(DocInfo docInfo, DocumentFormat toFormat, 
					String toContentType, String storeBaseLocation, DocumentStore<SDM, SD> sourceDocStore, 
					DocumentStore<?, ?> targetDocStore, DocumentTxStrategy docTxStrategy, DocNameStrategy docNameStrategy) throws IOException {

		List<DocInfo> docInfos = null;
		
		DocumentFormat docFormat = formatResolver.resolve(docInfo);
		
		DocConverter converter = converterFactor.getConverter(docFormat, toFormat);
		
		if (converter == null) {
			
			LOGGER.info("No document converter found for [{}] to [{}] conversion", docFormat, toFormat);
			
		} else {
			
			IDocument sourceDoc = sourceDocStore.load(docInfo);
			
			DocStoreWriter docWriter = new DocStoreWriter(targetDocStore, storeBaseLocation, toContentType);
			DocumentStream docStream = DocStreamBuilder.perPageDocStream(new ByteArrayStreamManager(), docWriter, docNameStrategy);
			
			converter.convertAndWrite(sourceDoc.getDataStream(), docFormat, docStream, toFormat);
			docInfos = docWriter.getDocPages();
		}
		
		return docInfos;
	}
	


}

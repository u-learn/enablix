package com.enablix.doc.preview.image;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocPreviewData;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.commons.dms.api.DocumentTransformer;
import com.enablix.commons.dms.api.DocumentTxStrategy;
import com.enablix.commons.dms.api.ImagePreview;
import com.enablix.commons.util.LocationUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.preview.DocPreviewGenerator;
import com.enablix.doc.preview.ThumbnailBuilder;
import com.enablix.doc.preview.ThumbnailBuilderFactory;
import com.enablix.doc.preview.ThumbnailSize;

@Component
public class ImagePreviewGenerator implements DocPreviewGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImagePreviewGenerator.class);
	
	private static final DocumentFormat PREVIEW_FORMAT = DocumentFormat.PNG;
	private static final String PNG_MIME_TYPE = "image/png";
	
	@Autowired
	private ThumbnailBuilderFactory thumbnailBuilderFactory;
	
	@Autowired
	private DocumentTransformer docTx;
	
	@Override
	public <SDM extends DocumentMetadata, SD extends Document<SDM>> 
			DocPreviewData createPreview(SDM docMetadata, DocumentStore<SDM, SD> sourceDocStore, 
					DocumentStore<? , ?> previewDocStore) throws IOException {

		ImagePreview previewData = null;
		
		LOGGER.debug("Creating image preview for document [{}]", docMetadata.getName());
		
		String storeBaseLocation = LocationUtil.getBaseFolder(docMetadata.getLocation()) + "/preview";
		
		List<DocInfo> convertedDocInfo = docTx.convertAndSaveToDocStore(
				docMetadata, PREVIEW_FORMAT, PNG_MIME_TYPE, storeBaseLocation, sourceDocStore, 
				previewDocStore, DocumentTxStrategy.PER_PAGE_DOC, (pgNum) -> docMetadata.getIdentity() + "-" + pgNum + ".png");  
		
		if (CollectionUtil.isNotEmpty(convertedDocInfo)) {
			
			previewData = new ImagePreview();
			previewData.setDocIdentity(docMetadata.getIdentity());
			previewData.setParts(convertedDocInfo);

			DocInfo firstImage = convertedDocInfo.get(0);
			
			ThumbnailBuilder tnBuilder = thumbnailBuilderFactory.getBuilder(PREVIEW_FORMAT);
			
			if (tnBuilder == null) {
				LOGGER.error("Thumbnail builder not found for format [{}]", PREVIEW_FORMAT);
				throw new IllegalStateException("Thumbnail builder not found for format [" + PREVIEW_FORMAT + "]");
			}
			
			DocInfo sThumbnail = tnBuilder.createAndSaveThumbnail(ThumbnailSize.SMALL, firstImage, sourceDocStore, previewDocStore);
			previewData.setSmallThumbnail(sThumbnail);
			
			DocInfo lThumbnail = tnBuilder.createAndSaveThumbnail(ThumbnailSize.LARGE, firstImage, sourceDocStore, previewDocStore);
			previewData.setLargeThumbnail(lThumbnail);
			
		} else {
			LOGGER.error("Unable to generate preview images for document [{}]", docMetadata.getName());
		}
			
		return previewData;
	}

	
	
}

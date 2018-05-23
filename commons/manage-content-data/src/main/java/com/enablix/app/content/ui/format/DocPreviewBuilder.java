package com.enablix.app.content.ui.format;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.share.SharedContentUrlCreator;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.commons.dms.api.DocPreviewData;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.commons.util.EnvPropertiesUtil;
import com.enablix.commons.util.FileUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.DocInfo;
import com.enablix.core.ui.ContentPreviewInfo;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.DocNoPreviewInfo;
import com.enablix.core.ui.DocRef;
import com.enablix.core.ui.ImagePreviewInfo;
import com.enablix.core.ui.ImagesBasedPreviewInfo;
import com.enablix.doc.preview.DocPreviewService;
import com.enablix.services.util.ContentDataUtil;

@Component
public class DocPreviewBuilder implements PreviewBuilder {

	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private DocPreviewService previewService;
	
	@Autowired
	private SharedContentUrlCreator urlCreator;
	
	@Override
	public ContentPreviewInfo buildPreview(DisplayableContent displayRecord, DisplayContext ctx) {
		
		ContentPreviewInfo previewInfo = null;
		
		DocRef doc = displayRecord.getDoc();
		
		if (doc != null) {
		
			DocumentMetadata docMd = docManager.getDocumentMetadata(doc.getDocIdentity());
			
			if (docMd != null) {
			
				if (ContentDataUtil.isImageContentType(docMd.getContentType())) {
					
					ImagePreviewInfo imagePreview = new ImagePreviewInfo();
					imagePreview.setImageUrl(doc.getAccessUrl());
					previewInfo = imagePreview;
					
				} else {
				
					if (docMd.getPreviewStatus() == PreviewStatus.AVAILABLE) {
						previewInfo = buildImagesPreview(docMd);
					}
					
					if (previewInfo == null) {
						previewInfo = buildNoPreviewInfo(displayRecord, docMd);
					}
				}
			}
		}
		
		return previewInfo;
	}

	private ContentPreviewInfo buildNoPreviewInfo(DisplayableContent displayRecord, DocumentMetadata docMd) {
		DocNoPreviewInfo noPreview = new DocNoPreviewInfo();
		String fileExt = FileUtil.getFileExt(docMd.getName());
		noPreview.setDocType(fileExt);
		return noPreview;
	}

	private ContentPreviewInfo buildImagesPreview(DocumentMetadata docMd) {
		
		ImagesBasedPreviewInfo previewInfo = null;
		
		String docIdentity = docMd.getIdentity();
		DocPreviewData previewData = previewService.getPreviewData(docIdentity);

		if (previewData != null) {
		
			List<DocInfo> parts = previewData.getParts();
			
			if (CollectionUtil.isNotEmpty(parts)) {
			
				List<String> imagesUrl = new ArrayList<>();
				
				for (int i = 0; i < parts.size(); i++) {
					String partImgUrl = createPartImageUrl(docIdentity, i);
					imagesUrl.add(partImgUrl);
				}
				
				previewInfo = new ImagesBasedPreviewInfo();
				previewInfo.setImageUrls(imagesUrl);
			}
		}
		
		return previewInfo;
	}
	
	

	private String createPartImageUrl(String docIdentity, int i) {
		String url = "/doc/pdp/" + docIdentity + "/" + i + "/";
		return EnvPropertiesUtil.getProperties().getServerUrl() 
				+ urlCreator.createShareableUrl(url, "content-preview", true);
	}

	@Override
	public boolean canHandle(DisplayableContent displayRecord) {
		return displayRecord.getDoc() != null;
	}

}

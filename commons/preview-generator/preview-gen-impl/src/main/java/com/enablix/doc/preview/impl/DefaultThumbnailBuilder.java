package com.enablix.doc.preview.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.BasicDocInfo;
import com.enablix.commons.dms.api.BasicDocument;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.commons.util.IOUtil;
import com.enablix.commons.util.LocationUtil;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.DocumentFormat;
import com.enablix.core.api.IDocument;
import com.enablix.doc.preview.ThumbnailBuilder;
import com.enablix.doc.preview.ThumbnailSize;

import net.coobird.thumbnailator.Thumbnails;

@Component
public class DefaultThumbnailBuilder implements ThumbnailBuilder {

	@Override
	public DocInfo createAndSaveThumbnail(ThumbnailSize size, DocInfo thumbnailFrom, DocumentStore<?, ?> sourceDocStore,
			DocumentStore<?, ?> destDocStore) throws IOException {
		
		DocInfo thmbnailInfo = thumbnailFrom;
		
		if (size.getMaxWidth() > 0) {
			
			IDocument doc = sourceDocStore.load(thumbnailFrom);
			ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
			
			InputStream inputStream = doc.getDataStream();
			
			try {
				
				BufferedImage inImage = ImageIO.read(inputStream);
		        
				int scaledWidth = size.getMaxWidth();
		    	double percent = ((double) scaledWidth) / inImage.getWidth();
		        int scaledHeight = (int) (inImage.getHeight() * percent);
	
				String basePath = LocationUtil.getBaseFolder(thumbnailFrom.getLocation());
				String docName = getThumnailDocName(thumbnailFrom.getName(), size);
				
				BasicDocInfo docInfo = new BasicDocInfo();
				docInfo.setContentLength(byteOS.size());
				docInfo.setName(docName);
				docInfo.setLocation(basePath + "/" + docName);
				docInfo.setContentType(thumbnailFrom.getContentType());
				docInfo.addProperties(IOUtil.getImageProperties(scaledWidth, scaledHeight));
				
				Thumbnails.of(inImage).outputFormat(DocumentFormat.PNG.toString().toLowerCase())
							.size(scaledWidth, scaledHeight).toOutputStream(byteOS);
				
				BasicDocument tnDoc = new BasicDocument(docInfo, new ByteArrayInputStream(byteOS.toByteArray()));
				
				thmbnailInfo = destDocStore.save(tnDoc, basePath);
				
			} finally {
				IOUtil.closeStream(byteOS);
				IOUtil.closeStream(inputStream);
			}
		}
		
		return thmbnailInfo;
	}
	

	@Override
	public boolean canBuildFrom(DocumentFormat from) {
		return from == DocumentFormat.PNG;
	}
	
	private String getThumnailDocName(String docName, ThumbnailSize size) {
		String ext = docName.substring(docName.lastIndexOf(".")+1);
		String fileSuffix = "_" + size.getSuffix() + "." + ext;
        return docName.replaceFirst(".([a-z]+)$", fileSuffix);
	}

}

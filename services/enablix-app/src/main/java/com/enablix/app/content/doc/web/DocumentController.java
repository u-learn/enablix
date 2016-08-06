package com.enablix.app.content.doc.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.commons.dms.api.ContentLengthAwareDocument;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.Actor;
import com.enablix.core.domain.activity.NonRegisteredActor;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.security.SecurityUtil;
import com.enablix.services.util.ActivityLogger;

@RestController
@RequestMapping("doc")
public class DocumentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);
	
	/**
     * Size of a byte buffer to read/write file
     */
    private static final int BUFFER_SIZE = 4096;
	
	@Autowired
	private DocumentManager docManager;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody DocumentMetadata handleFileUpload(
    		@RequestParam(value = "fileSize", required = true) long fileSize,
    		@RequestParam(value = "contentQId", required = true) String contentQId,
    		@RequestParam(value = "parentIdentity", required = false) String parentIdentity,
    		@RequestParam(value = "containerIdentity", required = false) String containerIdentity,
    		@RequestParam(value = "docIdentity", required = false) String docIdentity,
            @RequestParam(value="file", required = true) MultipartFile file) {
        
        try {
            Document<DocumentMetadata> document = docManager.buildDocument(file.getInputStream(), 
            		file.getOriginalFilename(), file.getContentType(), contentQId, fileSize, docIdentity);
            
            return saveDocument(contentQId, parentIdentity, containerIdentity, document);
            
        } catch (RuntimeException e) {
        	LOGGER.error("Error while uploading.", e);
            throw e;
            
        } catch (Exception e) {
        	LOGGER.error("Error while uploading.", e);
            throw new RuntimeException(e);
        }      
    }

	private DocumentMetadata saveDocument(String contentQId, 
			String parentIdentity, String containerIdentity,
			Document<?> document) throws IOException {
		
		String containerQId = getContainerQId(contentQId);
		
		if (!StringUtil.isEmpty(parentIdentity)) {
			docManager.saveUsingParentInfo(document, containerQId, parentIdentity);
		} else {
			docManager.saveUsingContainerInfo(document, containerQId, containerIdentity);
		}
		
		return document.getMetadata();
	}

	private String getContainerQId(String contentQId) {
		String parentQId = QIdUtil.getParentQId(contentQId); // doc container QId
		return parentQId;
	}
	
	
	/**
     * Method for handling file download request from client
     */
    @RequestMapping(value = "/download/{docIdentity}", method = RequestMethod.GET)
    public void doDownload(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String docIdentity, 
            @RequestParam(required=false) String atChannel) throws IOException {
 
    	Document<DocumentMetadata> doc = docManager.load(docIdentity);
    	
        // get MIME type of the file
        String mimeType = doc.getMetadata().getContentType(); //context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
 
        // set content attributes for the response
        response.setContentType(mimeType);
        
        if (doc instanceof ContentLengthAwareDocument) {
        	response.setContentLength((int) ((ContentLengthAwareDocument) doc).getContentLength());
        }
 
        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                doc.getMetadata().getName());
        response.setHeader(headerKey, headerValue);
 
        // get output stream of the response
        OutputStream outStream = response.getOutputStream();
 
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
 
        // write bytes read from the input stream into the output stream
        InputStream inputStream = doc.getDataStream();
        
		while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
 
        inputStream.close();
        outStream.close();
        
        // Audit download activity
        auditActivity(docIdentity, atChannel, doc);
 
    }

	private void auditActivity(String docIdentity, String atChannel, Document<DocumentMetadata> doc) {
		
		Channel channel = Channel.parse(atChannel);
        
		if (channel != null) {
        
			String userId = ProcessContext.get().getUserId();
			Actor actor = null;
        	
        	if (SecurityUtil.isGuestUserLogIn()) {
        		actor = new NonRegisteredActor(userId);
        	} else {
        		actor = new RegisteredActor(userId);
        	}
        	
        	ActivityLogger.auditDocDownload(doc.getMetadata().getContentQId(), 
        			null, docIdentity, channel, actor);
        }
	}
	
}

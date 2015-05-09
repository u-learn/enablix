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
import com.enablix.commons.dms.disk.DiskDocument;

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
            @RequestParam(value="file", required = true) MultipartFile file) {
        
        try {
            DiskDocument document = new DiskDocument(file.getInputStream(), 
            		file.getOriginalFilename(), file.getContentType());
            docManager.save(document);
            return document.getMetadata();
            
        } catch (RuntimeException e) {
        	LOGGER.error("Error while uploading.", e);
            throw e;
            
        } catch (Exception e) {
        	LOGGER.error("Error while uploading.", e);
            throw new RuntimeException(e);
        }      
    }
	
	/**
     * Method for handling file download request from client
     */
    @RequestMapping(value = "/download/{docIdentity}", method = RequestMethod.GET)
    public void doDownload(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String docIdentity) throws IOException {
 
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
 
    }
	
}

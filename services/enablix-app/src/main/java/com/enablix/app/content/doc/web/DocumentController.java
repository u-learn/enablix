package com.enablix.app.content.doc.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.disk.DiskDocument;

@RestController
@RequestMapping("doc")
public class DocumentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);
	
	@Autowired
	private DocumentManager docManager;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody DocumentMetadata handleFileUpload(
            @RequestParam(value="file", required = true) MultipartFile file) {
        
        try {
            DiskDocument document = new DiskDocument(file.getInputStream(), file.getOriginalFilename());
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
	
}

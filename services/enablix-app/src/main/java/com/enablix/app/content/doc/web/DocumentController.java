package com.enablix.app.content.doc.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.enrich.EmbeddedUrl;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.DocPreviewData;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.ContentLengthAwareDocument;
import com.enablix.core.api.IDocument;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.Actor;
import com.enablix.core.domain.activity.NonRegisteredActor;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.security.SecurityUtil;
import com.enablix.doc.preview.DocPreviewService;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;

@RestController
@RequestMapping("doc")
public class DocumentController {

	private static final String TEXT_CONTENT_IMG = "/assets/images/icons/text-icon.png";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);
	
	/**
     * Size of a byte buffer to read/write file
     */
    private static final int BUFFER_SIZE = 4096;
	
	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private DocPreviewService previewService;
	
	@Autowired
	private ContentDataManager dataManager;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody DocumentMetadata handleFileUpload(
    		@RequestParam(value = "fileSize", required = true) long fileSize,
    		@RequestParam(value = "contentQId", required = false) String contentQId,
    		@RequestParam(value = "parentIdentity", required = false) String parentIdentity,
    		@RequestParam(value = "containerIdentity", required = false) String containerIdentity,
    		@RequestParam(value = "docIdentity", required = false) String docIdentity,
    		@RequestParam(value = "temporary", required = false) boolean temporary,
    		@RequestParam(value = "generatePreview", required = false) boolean generatePreview,
            @RequestParam(value="file", required = true) MultipartFile file) {
        
        try {
            Document<DocumentMetadata> document = 
            		docManager.buildDocument(file.getInputStream(), file.getOriginalFilename(), 
            				file.getContentType(), contentQId, fileSize, docIdentity, temporary);
            
            DocumentMetadata docMd = saveDocument(contentQId, parentIdentity, containerIdentity, document, generatePreview);
			
            auditActivity(ActivityType.DOC_UPLOAD, docIdentity, docMd, 
            				null, null, null, null);
            
            return docMd;
            
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
			Document<?> document, boolean generatePreview) throws IOException {
		
		String containerQId = getContainerQId(contentQId);
		DocumentMetadata docMd = null;
		
		if (!StringUtil.isEmpty(parentIdentity)) {
			docMd = docManager.saveUsingParentInfo(document, containerQId, parentIdentity, generatePreview);
		} else {
			docMd = docManager.saveUsingContainerInfo(document, containerQId, containerIdentity, generatePreview);
		}
		
		return docMd;
	}

	private String getContainerQId(String contentQId) {
		return StringUtil.isEmpty(contentQId) ? "" : QIdUtil.getParentQId(contentQId); // doc container QId
	}
	
	
	/**
     * Method for handling file download request from client
     */
    @RequestMapping(value = "/download/{docIdentity}.{ext}", method = {RequestMethod.GET, RequestMethod.HEAD})
    public void doDownloadWithExt(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String docIdentity, 
            @RequestParam(required=false) String atChannel,
            @RequestParam(required=false) String atContext,
            @RequestParam(required=false) String atContextId,
            @RequestParam(required=false) String atContextTerm) throws IOException {
    	
    	downloadAction(request, response, docIdentity, atChannel, atContext, 
    			atContextId, atContextTerm, ActivityType.DOC_DOWNLOAD);
    }
    
    /**
     * Method for handling file download request from client
     */
    @RequestMapping(value = "/preview/{docIdentity}.{ext}", method = {RequestMethod.GET, RequestMethod.HEAD})
    public void doPreviewWithExt(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String docIdentity, 
            @RequestParam(required=false) String atChannel,
            @RequestParam(required=false) String atContext,
            @RequestParam(required=false) String atContextId,
            @RequestParam(required=false) String atContextTerm) throws IOException {
    	
    	downloadAction(request, response, docIdentity, atChannel, atContext, 
    			atContextId, atContextTerm, ActivityType.DOC_PREVIEW);
    }
	
    /**
     * Method for handling file download request from client
     * @return 
     */
    @RequestMapping(value = "/docmd/{docIdentity}", method = {RequestMethod.GET, RequestMethod.HEAD},
    				produces="application/json")
    public DocumentMetadata getDocMetadata(@PathVariable String docIdentity) {
    	return docManager.loadDocMetadata(docIdentity);
    }
    
    @RequestMapping(value = "/pd/{docIdentity}", method = {RequestMethod.GET},
			produces="application/json")
	public DocPreviewData getDocPreviewData(@PathVariable String docIdentity,
			@RequestParam(required=false) String atChannel,
            @RequestParam(required=false) String atContext,
            @RequestParam(required=false) String atContextId,
            @RequestParam(required=false) String atContextTerm) {
    	
    	DocPreviewData previewData = previewService.getPreviewData(docIdentity);
    	
    	if (previewData != null) {
    		DocumentMetadata doc = docManager.getDocumentMetadata(docIdentity);
    		auditActivity(ActivityType.DOC_PREVIEW, docIdentity, doc, 
        		Channel.WEB.toString(), atContext, atContextId, atContextTerm);
    	}
    	
		return previewData;
	}
   
    @RequestMapping(value = "/pdp/{docIdentity}/{elementIndx}/", method = {RequestMethod.GET})
	public void getDocPreviewPart(HttpServletRequest request,
            HttpServletResponse response, 
            @PathVariable String docIdentity, @PathVariable int elementIndx) throws IOException {
    	
    	IDocument part = previewService.getPreviewDataPart(docIdentity, elementIndx);
    	
    	if (part != null) {
            sendDownloadResponse(response, part);
    	} else {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND);
    	}
    	
	}   
    
    @RequestMapping(value = "/sthmbnl/{docIdentity}/", method = {RequestMethod.GET})
   	public void getPreviewDataSmallThumbnail(HttpServletRequest request,
               HttpServletResponse response, 
               @PathVariable String docIdentity ) throws IOException {
       	
       	IDocument part = previewService.getDocSmallThumbnail(docIdentity);
       	
       	if (part != null) {
               sendDownloadResponse(response, part);
       	} else {
       		response.sendError(HttpServletResponse.SC_NOT_FOUND);
       	}
       	
   	}
    
    @RequestMapping(value = "/icon/r/{contentQId}/{recIdentity}/", method = {RequestMethod.GET})
   	public void getPreviewDataIcon(HttpServletRequest request,
               HttpServletResponse response, 
               @PathVariable String contentQId,
               @PathVariable String recIdentity ) throws IOException {
       	
    	boolean iconFound = false;
    	
    	TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
    	ContentDataRef dataRef = ContentDataRef.createContentRef(
    			template.getId(), contentQId, recIdentity, null);
    	
    	Map<String, Object> contentRecord = dataManager.getContentRecord(dataRef, template, DataViewUtil.allDataView());
    	ContainerType container = template.getContainerDefinition(contentQId);

    	if (CollectionUtil.isNotEmpty(contentRecord)) {
    		
			String docIdentity = ContentDataUtil.findDocIdentity(contentRecord, container);
    		
    		if (StringUtil.hasText(docIdentity)) {
    		
    			IDocument part = previewService.getDocSmallThumbnail(docIdentity);
    			if (part != null) {
    				sendDownloadResponse(response, part);
    				iconFound = true;
    			} 
    		} else {
    			// check url
    			String previewImgUrl = findUrlPreviewImageUrl(contentRecord);
    			if (StringUtil.hasText(previewImgUrl)) {
    				response.sendRedirect(previewImgUrl);
    				iconFound = true;
    			}
    		}
       	}
    	
    	if (!iconFound) {

    		if (template.isTextContainer(container)) {
    			response.sendRedirect(TEXT_CONTENT_IMG);
    		} else {
    			response.sendRedirect("/assets/images/icons/one-px.svg");
    		}
    	}
       	
   	}
    
    @SuppressWarnings("unchecked")
	public String findUrlPreviewImageUrl(Map<String, Object> record) {
		
    	Object urls = record.get(ContentDataConstants.CONTENT_URLS_KEY);
		
		if (urls != null) {
			List<EmbeddedUrl> urlList = (List<EmbeddedUrl>) urls;
			if (urlList.size() > 0) {
				EmbeddedUrl eUrl = urlList.get(0);
				return eUrl.getPreviewImageUrl();
			}
		}
		
		return null;
	}
    
    @RequestMapping(value = "/lthmbnl/{docIdentity}/", method = {RequestMethod.GET})
   	public void getPreviewDataLargeThumbnail(HttpServletRequest request,
               HttpServletResponse response, 
               @PathVariable String docIdentity) throws IOException {
       	
       	IDocument part = previewService.getDocLargeThumbnail(docIdentity);
       	
       	if (part != null) {
               sendDownloadResponse(response, part);
       	} else {
       		response.sendError(HttpServletResponse.SC_NOT_FOUND);
       	}
       	
   	}
    
    
	/**
     * Method for handling file download request from client
     */
    @RequestMapping(value = "/download/{docIdentity}", method = {RequestMethod.GET, RequestMethod.HEAD})
    public void doDownload(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String docIdentity, 
            @RequestParam(required=false) String atChannel,
            @RequestParam(required=false) String atContext,
            @RequestParam(required=false) String atContextId,
            @RequestParam(required=false) String atContextTerm) throws IOException {
    	
    	downloadAction(request, response, docIdentity, atChannel, atContext, 
    			atContextId, atContextTerm, ActivityType.DOC_DOWNLOAD);
    }
    
    @RequestMapping(value = "/preview/{docIdentity}", method = {RequestMethod.GET, RequestMethod.HEAD})
    public void doPreview(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String docIdentity, 
            @RequestParam(required=false) String atChannel,
            @RequestParam(required=false) String atContext,
            @RequestParam(required=false) String atContextId,
            @RequestParam(required=false) String atContextTerm) throws IOException {
    	
    	downloadAction(request, response, docIdentity, atChannel, atContext, 
    			atContextId, atContextTerm, ActivityType.DOC_PREVIEW);
    }
    
    public void downloadAction(HttpServletRequest request, HttpServletResponse response, 
    		String docIdentity, String atChannel, String atContext, String atContextId, 
    		String atContextTerm, ActivityType activityType) throws IOException {
 
    	Document<DocumentMetadata> doc = docManager.load(docIdentity);
    	
        sendDownloadResponse(response, doc);
        
        if (!request.getMethod().equals(RequestMethod.HEAD.toString())) {
        	// Audit download activity
        	auditActivity(activityType, docIdentity, doc.getMetadata(), 
        		atChannel, atContext, atContextId, atContextTerm);
        }
 
    }

	private void sendDownloadResponse(HttpServletResponse response, IDocument doc) throws IOException {
		
		// get MIME type of the file
        String mimeType = doc.getDocInfo().getContentType(); //context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
 
        // set content attributes for the response
        response.setContentType(mimeType);
        
        if (doc instanceof ContentLengthAwareDocument) {
        	response.setContentLength((int) ((ContentLengthAwareDocument) doc).getContentLength());
        }
 
        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                doc.getDocInfo().getName());
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

	private void auditActivity(ActivityType activityType, String docIdentity, 
			DocumentMetadata docMd, String atChannel, String contextName, 
			String contextId, String contextTerm) {
		
		Channel channel = Channel.parse(atChannel);
        
		if (channel != null) {
        
			ProcessContext pc = ProcessContext.get();
			String userId = pc.getUserId();
			Actor actor = null;
        	
        	if (SecurityUtil.isGuestUserLogIn()) {
        		actor = new NonRegisteredActor(userId, pc.getUserDisplayName());
        	} else {
        		actor = new RegisteredActor(userId, pc.getUserDisplayName());
        	}
        	
        	ActivityLogger.auditDocActivity(activityType, docMd.getContentQId(), 
        			null, docIdentity, channel, actor, contextName, contextId, contextTerm, 
        			docMd.getName());
        }
	}
	
}

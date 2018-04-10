package com.enablix.app.template.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.template.service.ContainerAndTemplate;
import com.enablix.app.template.service.ContainerOrder;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.exception.ValidationException;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

@RestController
@RequestMapping("template")
public class TemplateController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);
	
	@Autowired
	private TemplateManager templateMgr;
	
	@RequestMapping(method = RequestMethod.POST, value="/upload", consumes = "application/xml")
	public String importTemplate(HttpServletRequest request,
			@RequestParam String filename) throws IOException, JAXBException {
		LOGGER.debug("Importing content template");
		templateMgr.saveXml(request.getInputStream(), filename);
		return "success";
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{templateId}/container/update", consumes = "application/json")
	public ContentTemplate updateContainer(@PathVariable String templateId, 
			@RequestBody ContainerType container) throws ValidationException, Exception {
		LOGGER.debug("Update container definition");
		return templateMgr.updateContainer(templateId, container);
	}

	@RequestMapping(method = RequestMethod.POST, value="/{templateId}/container/add", consumes = "application/json")
	public ContainerAndTemplate addContainer(@PathVariable String templateId, 
			@RequestBody ContainerType container) throws ValidationException, Exception {
		LOGGER.debug("Adding container definition");
		return templateMgr.addContainer(templateId, container);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{templateId}/c/{containerQId}/")
	public ContainerAndTemplate deleteContainer(@PathVariable String templateId, 
			@PathVariable String containerQId) throws ValidationException, Exception {
		LOGGER.debug("Deleting container definition");
		return templateMgr.deleteContainer(templateId, containerQId);
	}

	@RequestMapping(method = RequestMethod.GET, value="/containers/{templateId}/{parentQId}", produces = "application/json")
	public List<ContainerType> fetchContainerNames(@PathVariable String templateId, 
			@PathVariable String parentQId) {
		return templateMgr.getChildContainers(templateId, parentQId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/containers/{templateId}", produces = "application/json")
	public List<ContainerType> fetchRootContainerNames(@PathVariable String templateId) {
		return fetchContainerNames(ProcessContext.get().getTemplateId(), null);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{templateId}", produces = "application/json")
	public ContentTemplate getContentTemplate(@PathVariable String templateId) {
		return templateMgr.getTemplate(templateId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/default", produces = "application/json")
	public ContentTemplate getContentTemplate() {
		return templateMgr.getTemplate(ProcessContext.get().getTemplateId());
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{templateId}/container/order", consumes = "application/json")
	public ContentTemplate updateContainerOrder(@PathVariable String templateId, 
			@RequestBody ContainerOrder order) throws ValidationException, Exception {
		LOGGER.debug("Updating container order");
		return templateMgr.updateContainerOrder(order, templateId);
	}
}

package com.enablix.app.template.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.core.commons.xsdtopojo.ContainerType;

@RestController
@RequestMapping("template")
public class TemplateController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);
	
	@Autowired
	private TemplateManager templateMgr;
	
	@RequestMapping(method = RequestMethod.POST, value="/upload", consumes = "application/xml")
	public String importTemplate(HttpServletRequest request) throws IOException, JAXBException {
		LOGGER.debug("Importing content template");
		templateMgr.saveXml(request.getInputStream());
		return "success";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/containers/{templateId}/{parentQId}", produces = "application/json")
	public List<ContainerType> fetchContainerNames(@PathVariable String templateId, 
			@PathVariable String parentQId) {
		return templateMgr.getChildContainers(templateId, parentQId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/containers/{templateId}", produces = "application/json")
	public List<ContainerType> fetchRootContainerNames(@PathVariable String templateId) {
		return fetchContainerNames(templateId, null);
	}
	
}

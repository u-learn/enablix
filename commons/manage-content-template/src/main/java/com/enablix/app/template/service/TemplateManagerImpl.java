package com.enablix.app.template.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.exception.AppError;
import com.enablix.commons.exception.ValidationException;
import com.enablix.commons.util.EnvPropertiesUtil;
import com.enablix.commons.util.IOUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.commons.validate.Validators;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsd.parser.ContentTemplateXMLParser;
import com.enablix.core.commons.xsd.parser.XMLParser;
import com.enablix.core.commons.xsd.parser.XMLParserRegistry;
import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;
import com.enablix.core.commons.xsdtopojo.UiDefinitionType;
import com.enablix.core.domain.content.TemplateDocument;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.mongo.counter.Counter;
import com.enablix.core.mongo.counter.CounterFactory;
import com.enablix.core.mongo.counter.ExplicitCounterRegistry;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.TemplateWrapper;

@Service
public class TemplateManagerImpl implements TemplateManager {

	private static final String CONTAINER_COUNTER_NAME = "CT_CONTAINER_COUNTER";

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateManagerImpl.class); 
	
	@Autowired
	private TemplateCrudService crudService;
	
	@Autowired
	private XMLParserRegistry xmlParserRegistry;
	
	@Autowired
	private TemplateVersionManager templateVersionManager;
	
	@Autowired
	private TemplateCache templateCache;
	
	@Autowired
	private ContentTemplateXMLParser templateParser;
	
	@Autowired
	private TenantRepository tenantRepo;
	
	@Autowired
	private CounterFactory counterFactory;
	
	@Autowired
	private ExplicitCounterRegistry counterRegistry;
	
	@Autowired
	private ContainerRecordCountValidator contRecCntValidator;
	
	private Counter containerCounter;
	
	@PostConstruct
	public void init() {
		this.counterRegistry.register(CONTAINER_COUNTER_NAME);
		this.containerCounter = counterFactory.getCounter(CONTAINER_COUNTER_NAME);
	}
	
	@Override
	public void save(ContentTemplate template, String filename) {

		checkAndUpdateDisplayOrder(template);
		
		TemplateDocument templateDoc = new TemplateDocument();
		templateDoc.setFilename(filename);
		templateDoc.setTemplate(template);
		templateDoc.setIdentity(template.getId());
		
		crudService.saveOrUpdate(templateDoc);
		
		updateTemplateInCache(template);
		templateVersionManager.updateTemplateVersion(template);
		
		EventUtil.publishEvent(new Event<ContentTemplate>(Events.CONTENT_TEMPLATE_UPDATED, template));
	}
	
	private void checkAndUpdateDisplayOrder(ContentTemplate template) {
		BigInteger displayOrder = template.getDataDefinition().getContainer().get(0).getDisplayOrder();
		if (displayOrder == null) {
			TemplateBuilder.initContainerOrder(template);
		}
	}

	private void updateTemplateInCache(ContentTemplate template) {
		updateTemplateInCache(new TemplateWrapper(template));
	}
	
	private void updateTemplateInCache(TemplateFacade templateWrapper) {
		templateCache.put(ProcessContext.get().getTenantId(), templateWrapper);
	}
	
	@Override
	public ContentTemplate getTemplate(String templateId) {
		TemplateFacade templateWrapper = getTemplateFacade(templateId);
		return templateWrapper == null ? null : templateWrapper.getTemplate();
	}
	
	@Override
	public TemplateFacade getTemplateFacade(String templateId) {
		
		TemplateFacade templateFacade = templateCache.getTemplate(ProcessContext.get().getTenantId(), templateId);
		
		if (templateFacade == null) {
		
			TemplateDocument templateDoc = crudService.findByIdentity(templateId);
			if (templateDoc != null) {
				templateFacade = new TemplateWrapper(templateDoc.getTemplate());
				updateTemplateInCache(templateFacade);
			}
			
		} 
		
		return templateFacade;
	}

	@Override
	public DataDefinitionType getDataDefinition(String templateId) {
		ContentTemplate template = getTemplate(templateId);
		return template == null ? null : template.getDataDefinition();
	}

	@Override
	public UiDefinitionType getUIDefinition(String templateId) {
		ContentTemplate template = getTemplate(templateId);
		return template == null ? null : template.getUiDefinition();
	}

	@Override
	public void saveXml(InputStream templateXmlInputStream, String filename) throws JAXBException {
		
		XMLParser<ContentTemplate> parser = xmlParserRegistry.getXMLParser(ContentTemplate.class);
		if (parser == null) {
			LOGGER.error("No xml parser found for ContentTemplate");
			throw new IllegalStateException("No xml parser found for ContentTemplate");
		}
		
		ContentTemplate template = parser.unmarshal(templateXmlInputStream);

		save(template, filename);
	}

	@Override
	public List<ContainerType> getChildContainers(String templateId, String parentQId) {
		
		ContentTemplate template = getTemplate(templateId);
		
		if (template == null) {
			LOGGER.error("Invalid template id : {}", templateId);
			throw new IllegalArgumentException("Invalid template id [" + templateId + "]");
		}
		
		List<ContainerType> containers = new ArrayList<>();
		
		if (StringUtil.isEmpty(parentQId)) {
			containers = template.getDataDefinition().getContainer();
			
		} else {
			ContainerType parentContainer = 
					TemplateUtil.findContainer(template.getDataDefinition(), parentQId);
			containers = parentContainer.getContainer();
		}
		
		return containers;
	}
	
	@Override
	public ContainerAndTemplate addContainer(String templateId, ContainerType container) throws Exception {
		
		TemplateDocument templateDoc = crudService.findByIdentity(templateId);
		ContentTemplate template = templateDoc.getTemplate();
		
		updateContainerQId(container);
		
		TemplateBuilder.addContainerToTemplate(template, container);
		
		Tenant tenant = tenantRepo.findByTenantId(ProcessContext.get().getTenantId());
		persistOnFilesystem(template, tenant, templateDoc.getFilename());
		
		return new ContainerAndTemplate(container, template);
	}
	
	private void updateContainerQId(ContainerType container) {
		
		int nextValue = containerCounter.nextValue();
		String id = null;
		
		if (container.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_DIMENSION) {
			id = "dim" + nextValue;
		} else {
			id = "type" + nextValue;
		}
		
		container.setId(id);
		container.setQualifiedId(id);
		container.setDisplayOrder(BigInteger.valueOf(1000 + nextValue));
	}

	@Override
	public ContentTemplate updateContainer(String templateId, ContainerType container) throws Exception {
		
		TemplateDocument templateDoc = crudService.findByIdentity(templateId);
		ContentTemplate template = templateDoc.getTemplate();
		
		boolean labelUpdated = TemplateBuilder.updateContainerAndCheckLabelUpdate(template, container);
		
		Tenant tenant = tenantRepo.findByTenantId(ProcessContext.get().getTenantId());
		persistOnFilesystem(template, tenant, templateDoc.getFilename());
		
		if (labelUpdated) {
			EventUtil.publishEvent(new Event<ContainerType>(Events.CONTAINER_LABEL_UPDATED, container));
		}
		
		return template;
	}
	
	@Override
	public ContainerAndTemplate deleteContainer(String templateId, String containerQId) throws Exception {
		
		ContainerType cacheDef = getTemplateFacade(templateId).getContainerDefinition(containerQId);
		validateDelete(cacheDef);
		
		TemplateDocument templateDoc = crudService.findByIdentity(templateId);
		ContentTemplate template = templateDoc.getTemplate();
		
		ContainerType container = TemplateBuilder.removeContainer(template, containerQId);
		
		Tenant tenant = tenantRepo.findByTenantId(ProcessContext.get().getTenantId());
		persistOnFilesystem(template, tenant, templateDoc.getFilename());
		
		EventUtil.publishEvent(new Event<ContainerType>(Events.CONTAINER_DEF_REMOVED, container));
		
		return new ContainerAndTemplate(container, template);
	}
	
	private void validateDelete(ContainerType container) throws ValidationException {
		Collection<AppError> errors = Validators.list(contRecCntValidator).validate(container);
		if (CollectionUtil.isNotEmpty(errors)) {
			throw new ValidationException(errors);
		}
	}
	
	@Override
	public void persistOnFilesystem(ContentTemplate template, Tenant tenant, String filename) throws Exception {
		
		String destinationFolder = getDestinationFolder(tenant.getTenantId());
		
		if (!StringUtil.hasText(filename)) {
			
			String[] files = new File(destinationFolder).list((dir, name) -> name.toLowerCase().endsWith(".xml"));
			
			for (String file : files) {
				String filepath = destinationFolder + File.separator + file;
				FileInputStream fis = null;
				
				try {
					
					fis = new FileInputStream(filepath);
					ContentTemplate fileTemplate = templateParser.unmarshal(fis);
					
					if (fileTemplate.getId() == template.getId()) {
						filename = file;
						break;
					}
					
				} finally {
					IOUtil.closeStream(fis);
				}
				
			}
			
			if (!StringUtil.hasText(filename)) {
				// set the default file name
				filename = tenant.getDefaultTemplateId() + ".xml";
			}
		}
		
		String destLocation = destinationFolder + File.separator + filename; 
		saveXml(template, destLocation);
	}
	
	private void saveXml(ContentTemplate template, String location) throws Exception {
		
		File destFile = new File(location);
		
		if (destFile.exists()) {
			destFile.delete();
		}
		
		if (!destFile.createNewFile()) {
			throw new Exception("Unable to create file: " + location);
		}
		
		FileOutputStream fos = null;
		
		try {
			
			fos = new FileOutputStream(destFile);
			templateParser.marshal(template, fos);
			
		} finally {
			IOUtil.closeStream(fos);
		}
		
	}
	
	private String getDestinationFolder(String tenantId) {
		
		String destFolderLoc = EnvPropertiesUtil.getDataDirectory() 
				+ File.separator + AppConstants.TEMPLATES_DIR 
				+ File.separator + tenantId;
		
		File destFolder = new File(destFolderLoc);
		
		if (!destFolder.exists()) {
			destFolder.mkdirs();
		}
		
		return destFolderLoc;
	}

	@Override
	public ContentTemplate updateContainerOrder(ContainerOrder order, String templateId) throws Exception {
		
		TemplateDocument templateDoc = crudService.findByIdentity(templateId);
		ContentTemplate template = templateDoc.getTemplate();
		
		TemplateBuilder.updateContainerOrder(template, order);
		
		Tenant tenant = tenantRepo.findByTenantId(ProcessContext.get().getTenantId());
		persistOnFilesystem(template, tenant, templateDoc.getFilename());
		
		return template;
	}

}

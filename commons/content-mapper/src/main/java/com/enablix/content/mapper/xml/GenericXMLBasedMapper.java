package com.enablix.content.mapper.xml;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.mapper.ContentMapper;
import com.enablix.content.mapper.ContentSource;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.content.mapping.ContentMappingManager;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;
import com.enablix.core.commons.xsdtopojo.ContentMapping;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

@Component
public class GenericXMLBasedMapper implements ContentMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericXMLBasedMapper.class);
	
	private Map<ContentSource, ContentSource> supportedSources = new HashMap<>();
	
	@Autowired
	private ContentMappingManager mappingManager;
	
	@Autowired
	private MappingWorkerRegistry workerRegistry;
	
	@Override
	public EnablixContent transformToEnablixContent(ExternalContent content, ContentTemplate template) {
		
		ContentContainerMappingType contentMapping = mappingManager.getContentMapping(content.getContentSource(), content.getContentQId());
		
		if (contentMapping == null) {
			
			LOGGER.error("No mapper found for source [{}] :: content [{}]", 
					content.getContentSource(), content.getContentQId());
			
			throw new UnsupportedOperationException("No mapper found for source [" 
					+ content.getContentSource() + "] :: content [" + content.getContentQId() + "]");
		}
		
		EnablixContent ebxContent = new EnablixContent(
				new HashMap<String, Object>(), content.getContentQId(), template.getId());
		
		for (MappingWorker worker : workerRegistry.getAllWorkers()) {
			worker.execute(contentMapping, content, ebxContent, template);
		}
		
		return ebxContent;
	}

	@Override
	public boolean isSupported(ContentSource source) {
		return supportedSources.containsKey(source);
	}
	
	protected void register(ContentSource source) {
		supportedSources.put(source, source);
	}
	
	public void register(ContentMapping contentMapping) {
		String tenantId = ProcessContext.get().getTenantId();
		register(new ContentSource(contentMapping.getId(), tenantId));
	}
	
}

package com.enablix.analytics.info.detection.mapping;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.content.mapper.xml.GenericXMLBasedMapper;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingType;
import com.enablix.core.commons.xsdtopojo.ContentItemMappingsType;
import com.enablix.core.commons.xsdtopojo.SourceIdType;
import com.enablix.services.util.TemplateUtil;

@Component
public class SimpleInfoMapper {

	@Autowired
	private GenericXMLBasedMapper xmlBasedMapper;
	
	public EnablixContent transformToEnablixContent(
			ContainerType container, ExternalContent content, TemplateFacade template) {
		
		EnablixContent ebxContent = null;
		Map<String, Object> data = content.getData();
		
		Object title = data.get("title");
		Object id = data.get("id");
		if (title != null && id != null) {
			
			String titleAttr = template.getStudioLabelAttributeId(container.getQualifiedId());
			
			ContentContainerMappingType mapping = new ContentContainerMappingType();

			SourceIdType sourceId = new SourceIdType();
			sourceId.setValue("id");
			
			mapping.setSourceId(sourceId);

			ContentItemMappingsType itemMappings = new ContentItemMappingsType();
			mapping.setContentItemMappings(itemMappings);
			
			if (StringUtil.hasText(titleAttr)) {
				
				ContentItemMappingType titleMapping = new ContentItemMappingType();
				titleMapping.setItemId(titleAttr);
				titleMapping.setValue("title");
				
				itemMappings.getItemMapping().add(titleMapping);
			}
			
			Object desc = data.get("desc");
			
			if (desc != null) {
				
				String descAttr = TemplateUtil.getDescAttribute(container);
				
				if (StringUtil.hasText(descAttr)) {
					ContentItemMappingType descMapping = new ContentItemMappingType();
					descMapping.setItemId(descAttr);
					descMapping.setValue("desc");
					
					itemMappings.getItemMapping().add(descMapping);
				}
			}
			
			ebxContent = xmlBasedMapper.tranformUsingMapping(content, template, mapping);
			
		}
		
		return ebxContent;
	}

}

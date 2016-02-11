package com.enablix.core.commons.xsd.parser;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.commons.xsdtopojo.BaseContentType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;

public class ContentTemplateXMLParser extends DefaultXMLParser<ContentTemplate> {

	public ContentTemplateXMLParser(String xsdFileLocation) {
		super(xsdFileLocation, ContentTemplate.class);
		getUnmarshaller().setListener(new RelationshipRecorder());
	}

	@Override
	public ContentTemplate unmarshal(InputStream is) throws JAXBException {
		ContentTemplate template = super.unmarshal(is);
		populateQualifierId(template, ParentChildRelation.get());
		ParentChildRelation.clear();
		return template;
	}
	
	private void populateQualifierId(ContentTemplate template, ParentChildRelation relationships) {
		
		if (relationships != null) {
			
			DataDefinitionType dataDef = template.getDataDefinition();
			
			for (ContainerType container : dataDef.getContainer()) {
				
				if (StringUtil.isEmpty(container.getQualifiedId())) {
					container.setQualifiedId(container.getId());
				}
				
				populateQualifiedId(container, relationships.getChildren(container), relationships);
			}
			
		}
	}
	
	private void populateQualifiedId(BaseContentType parent, 
			List<BaseContentType> children, ParentChildRelation relationships) {
		
		if (children != null) {

			for (BaseContentType child : children) {
			
				if (StringUtil.isEmpty(child.getQualifiedId())) {
					child.setQualifiedId(createQualifiedId(parent, child));
				}
				
				populateQualifiedId(child, relationships.getChildren(child), relationships);
			}
		}
	}
	
	public String createQualifiedId(BaseContentType parent, BaseContentType child) {
		return QIdUtil.createQualifiedId(parent.getQualifiedId(), child.getId());
	}
	
}

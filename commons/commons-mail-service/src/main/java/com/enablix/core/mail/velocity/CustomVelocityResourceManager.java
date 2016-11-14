package com.enablix.core.mail.velocity;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.ResourceManagerImpl;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.mail.utility.MailConstants;

public class CustomVelocityResourceManager extends ResourceManagerImpl {

	public Resource getResource(final String resourceName, final int resourceType, final String encoding)
	        throws ResourceNotFoundException, ParseErrorException {
		
		if (resourceName.startsWith(MailConstants.BODY_TEMPLATE_PATH)
				|| resourceName.startsWith(MailConstants.SUBJECT_TEMPLATE_PATH)) {
			
			try {
				String tenantSpecificResource = ProcessContext.get().getTenantId() + "/" + resourceName;
				return super.getResource(tenantSpecificResource, resourceType, encoding);
				
			} catch (ResourceNotFoundException ex) {
				String defaultResource = "default/" + resourceName;
				return super.getResource(defaultResource, resourceType, encoding);
			}
			
		} else {
			return super.getResource(resourceName, resourceType, encoding);
		}
	}
	
}

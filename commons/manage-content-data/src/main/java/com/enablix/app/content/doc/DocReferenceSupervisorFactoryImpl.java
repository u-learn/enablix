package com.enablix.app.content.doc;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class DocReferenceSupervisorFactoryImpl extends SpringBackedAbstractFactory<DocReferenceSupervisor> implements DocReferenceSupervisorFactory {

	@Override
	public Collection<DocReferenceSupervisor> getSupervisors() {
		return registeredInstances();
	}

	@Override
	protected Class<DocReferenceSupervisor> lookupForType() {
		return DocReferenceSupervisor.class;
	}

}

package com.enablix.content.mapper.xml;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.ExecutionOrderComparator;
import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@Component
public class MappingWorkerRegistryImpl extends SpringBackedBeanRegistry<MappingWorker> implements MappingWorkerRegistry {

	private Set<MappingWorker> workers = 
			new TreeSet<>(new ExecutionOrderComparator());
	
	@Override
	public Collection<MappingWorker> getAllWorkers() {
		return workers;
	}

	@Override
	protected Class<MappingWorker> lookupForType() {
		return MappingWorker.class;
	}

	@Override
	protected void registerBean(MappingWorker bean) {
		workers.add(bean);
	}

}

package com.enablix.bayes.finding.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.bayes.finding.NodeFindingProvider;
import com.enablix.bayes.finding.NodeFindingProviderFactory;
import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@Component
public class NodeFindingProviderFactoryImpl extends SpringBackedBeanRegistry<NodeFindingProvider> implements NodeFindingProviderFactory {

	private Map<String, NodeFindingProvider> providers = new HashMap<>();
	
	@Override
	public NodeFindingProvider getFindingProvider(String nodeName) {
		return providers.get(nodeName);
	}

	@Override
	protected Class<NodeFindingProvider> lookupForType() {
		return NodeFindingProvider.class;
	}

	@Override
	protected void registerBean(NodeFindingProvider bean) {
		providers.put(bean.nodeName(), bean);
	}

}

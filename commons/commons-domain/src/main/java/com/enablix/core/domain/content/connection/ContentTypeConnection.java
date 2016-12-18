package com.enablix.core.domain.content.connection;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.Tag;
import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_content_type_connection")
public class ContentTypeConnection extends BaseDocumentEntity {

	private String connectionName;
	
	private String contentQId;
	
	private List<ContentValueConnection> connections;
	
	private ConnectionContext connectionContext;
	
	private ContainerBusinessCategoryType connectedContainerCategory;
	
	private Set<Tag> tags;
	
	// The containers which encapsulate the content represented by contentQId
	private List<String> holdingContainers;
	
	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String containerQId) {
		this.contentQId = containerQId;
	}

	public List<ContentValueConnection> getConnections() {
		return connections;
	}

	public void setConnections(List<ContentValueConnection> connections) {
		this.connections = connections;
	}

	public ConnectionContext getConnectionContext() {
		return connectionContext;
	}

	public void setConnectionContext(ConnectionContext connectionContext) {
		this.connectionContext = connectionContext;
	}

	public ContainerBusinessCategoryType getConnectedContainerCategory() {
		return connectedContainerCategory;
	}

	public void setConnectedContainerCategory(ContainerBusinessCategoryType connectedContainerCategory) {
		this.connectedContainerCategory = connectedContainerCategory;
	}

	public List<String> getHoldingContainers() {
		return holdingContainers;
	}

	public void setHoldingContainers(List<String> affectedContainers) {
		this.holdingContainers = affectedContainers;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
}

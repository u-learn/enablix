package com.enablix.core.domain.content;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_import_request")
public class ImportRequest extends BaseDocumentEntity {
	
	private String source;
	
	private Map<String, Object> sourceDetails;
	
	private List<ImportRecord> records;
	
	private ImportStatus status;
	
	public ImportRequest() {
		this.status = ImportStatus.PENDING;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Map<String, Object> getSourceDetails() {
		return sourceDetails;
	}

	public void setSourceDetails(Map<String, Object> sourceDetails) {
		this.sourceDetails = sourceDetails;
	}

	public List<ImportRecord> getRecords() {
		return records;
	}

	public void setRecords(List<ImportRecord> records) {
		this.records = records;
	}

	public ImportStatus getStatus() {
		return status;
	}

	public void setStatus(ImportStatus status) {
		this.status = status;
	}
	
}

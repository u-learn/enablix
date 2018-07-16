package com.enablix.app.content.update;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.domain.content.quality.AlertLevel;

public class UpdateContentRequest implements ContentUpdateContext {

	private static final String LINKAGE_CHANGE_KEY = "__linkageChange";
	private static final String DELETED_DOCS = "__deletedDocs";

	public enum QualityAlertProcessing {
		BREAK, 
		CONTINUE
	}
	
	private String templateId;
	
	private String parentIdentity;
	
	private String contentQId;
	
	private Map<String, Object> dataAsMap;
	
	private QualityAlertProcessing qualityAlertProcessing;
	
	private AlertLevel qualityAnalysisLevel;
	
	private List<LinkageChange> linkageChanges;

	protected UpdateContentRequest() {
		
	}
	
	public UpdateContentRequest(String templateId, 
			String contentQId, String jsonData) {
		this(templateId, null, contentQId, jsonData);
	}
	
	public UpdateContentRequest(String templateId, 
			String parentIdentity, String contentQId, String jsonData) {
		this(templateId, parentIdentity, contentQId, JsonUtil.jsonToMap(jsonData));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public UpdateContentRequest(String templateId, 
			String parentIdentity, String contentQId, Map<String, Object> dataAsMap) {
		super();
		this.templateId = templateId;
		this.parentIdentity = parentIdentity;
		this.contentQId = contentQId;
		this.dataAsMap = dataAsMap;
		
		if (dataAsMap.containsKey(LINKAGE_CHANGE_KEY)) {
			
			Object linkChanges = dataAsMap.remove(LINKAGE_CHANGE_KEY);
			
			if (linkChanges instanceof Collection) {
			
				this.linkageChanges = new ArrayList<>();
				
				((Collection) linkChanges).forEach((lc) -> {
				
					if (lc instanceof Map) {
						LinkageChange lcObj = JsonUtil.jsonToObject((Map<String, Object>)lc, LinkageChange.class);
						this.linkageChanges.add(lcObj);
					}
					
				});
			}
		}
		
		this.qualityAlertProcessing = QualityAlertProcessing.BREAK;
		this.qualityAnalysisLevel = AlertLevel.ERROR;
	}
	
	public String getTemplateId() {
		return templateId;
	}

	public String getParentIdentity() {
		return parentIdentity;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setParentIdentity(String recordId) {
		this.parentIdentity = recordId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public Map<String, Object> getDataAsMap() {
		return dataAsMap;
	}

	public boolean isUpdateAttribRequest() {
		return dataAsMap.containsKey(ContentDataConstants.IDENTITY_KEY);
	}
	
	public boolean isInsertRootRequest() {
		return !isUpdateAttribRequest() && StringUtil.isEmpty(parentIdentity);
	}
	
	public boolean isInsertChildRequest() {
		return !isUpdateAttribRequest() && !StringUtil.isEmpty(parentIdentity);
	}

	@Override
	public String parentIdentity() {
		return getParentIdentity();
	}

	@Override
	public String contentQId() {
		return getContentQId();
	}

	@Override
	public String templateId() {
		return getTemplateId();
	}

	public QualityAlertProcessing getQualityAlertProcessing() {
		return qualityAlertProcessing;
	}

	public void setQualityAlertProcessing(QualityAlertProcessing qualityAlertProcessing) {
		this.qualityAlertProcessing = qualityAlertProcessing;
	}

	public AlertLevel getQualityAnalysisLevel() {
		return qualityAnalysisLevel;
	}

	public void setQualityAnalysisLevel(AlertLevel qualityAnalysisLevel) {
		this.qualityAnalysisLevel = qualityAnalysisLevel;
	}

	public List<LinkageChange> getLinkageChanges() {
		return linkageChanges;
	}

	public void setLinkageChange(List<LinkageChange> linkageChange) {
		this.linkageChanges = linkageChange;
	}
	
}

package com.enablix.content.quality.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.commons.caching.api.CachingService;
import com.enablix.commons.util.tenant.TenantUtil;
import com.enablix.content.quality.QualityRule;
import com.enablix.content.quality.rule.ContentAttributeQualityRule.AttrCheckConfig;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ParamSetType;
import com.enablix.core.commons.xsdtopojo.QualityConfigType;
import com.enablix.core.domain.content.quality.AlertLevel;
import com.enablix.core.domain.content.quality.ContentAttribute;
import com.enablix.core.domain.content.quality.QualityAlert;
import com.enablix.core.domain.content.quality.QualityAlert.AlertSeverity;
import com.enablix.services.util.template.walker.TemplateContainerWalker;

public abstract class ContentAttributeQualityRule<C extends AttrCheckConfig> implements QualityRule, TemplateConfiguredRule {

	@Autowired
	protected CachingService cache;
	
	private String ruleId;
	private AlertLevel alertLevel;

	public ContentAttributeQualityRule(String ruleId, AlertLevel alertLevel) {
		super();
		this.ruleId = ruleId;
		this.alertLevel = alertLevel;
	}
	
	@Override
	public String getRuleId() {
		return ruleId;
	}

	@Override
	public AlertLevel getAlertLevel() {
		return alertLevel;
	}

	protected QualityAlert buildQualityAlert(String contentQId, ContentItemType item, 
			AlertLevel alertLevel, String alertMessage) {
		
		return QualityAlert.build(getRuleId(), 
				new ContentAttribute(contentQId, item.getId()), alertLevel, alertMessage);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<QualityAlert> evaluate(ParamSetType ruleConfig, 
			Map<String, Object> contentRecord, String contentQId, TemplateFacade template) {
		
		Collection<QualityAlert> alerts = new ArrayList<>();
		
		Object object = cache.get(TenantUtil.getTenantId(), cacheKey(contentQId, template.getId()));
		if (object != null && object instanceof ContainerCache) {

			ContainerCache<C> config = (ContainerCache<C>) object;
			
			for (C attrConfig : config.getCheckAttributes()) {
			
				ContentItemType item = attrConfig.getAttrDef();
				
				if (!isAttributeQualityGood(contentRecord, contentQId, template, item)) {
				
					QualityAlert alert = buildQualityAlert(contentQId, item, 
							attrConfig.getAlertLevel(), buildAlertMessage(attrConfig));
					alerts.add(alert);
				}
			}
		}
		
		return alerts;
	}
	
	protected abstract boolean isAttributeQualityGood(Map<String, Object> contentRecord, 
						String contentQId, TemplateFacade template, ContentItemType attrDef);
	
	protected abstract String buildAlertMessage(C attrConfig);
	
	
	
	@Override
	public void onTemplateUpdate(TemplateFacade template) {
		
		String tenantId = TenantUtil.getTenantId();
		TemplateContainerWalker itemWalker = new TemplateContainerWalker(template.getTemplate());
		
		itemWalker.walk((container) -> {
			
			ContainerCache<C> config = new ContainerCache<C>(container.getQualifiedId());
			
			for (ContentItemType item : container.getContentItem()) {
				
				QualityConfigType qualityConfig = item.getQualityConfig();
				
				if (qualityConfig != null) {
				
					C attrConfig = buildAttrCheckConfig(container, item);
					if (attrConfig != null) {
						config.addAttrConfig(attrConfig);
					}
					
				}
			}
			
			if (!config.getCheckAttributes().isEmpty()) {
				cache.put(tenantId, cacheKey(container.getQualifiedId(), template.getId()), config);
			}
			
		});
		
	}

	protected abstract C buildAttrCheckConfig(ContainerType container, ContentItemType item);

	protected String cacheKey(String containerQId, String templateId) {
		return templateId + "|" + getRuleId() + "|" + containerQId;
	}
	
	protected static class ContainerCache<C extends AttrCheckConfig> {
		
		private String containerQId;
		private List<C> checkAttributes = new ArrayList<>();
		
		public ContainerCache(String containerQId) {
			this.containerQId = containerQId;
		}
		
		public void addAttrConfig(C attrConfig) {
			checkAttributes.add(attrConfig);
		}

		public String getContainerQId() {
			return containerQId;
		}

		public List<C> getCheckAttributes() {
			return checkAttributes;
		}
		
	}
	
	public static class AttrCheckConfig {
		
		private ContentItemType attrDef;
		private String containerQId;
		private AlertLevel alertLevel;
		private AlertSeverity alertSeverity;

		public AttrCheckConfig(ContentItemType attrDef, 
				String containerQId, AlertLevel alertLevel, AlertSeverity alertSeverity) {
			
			super();
			this.attrDef = attrDef;
			this.containerQId = containerQId;
			this.alertLevel = alertLevel;
			this.alertSeverity = alertSeverity;
		}

		public String getAttrQId() {
			return attrDef.getQualifiedId();
		}

		public ContentItemType getAttrDef() {
			return attrDef;
		}

		public String getContainerQId() {
			return containerQId;
		}

		public AlertLevel getAlertLevel() {
			return alertLevel;
		}

		public AlertSeverity getAlertSeverity() {
			return alertSeverity;
		}
		
	}
	
}
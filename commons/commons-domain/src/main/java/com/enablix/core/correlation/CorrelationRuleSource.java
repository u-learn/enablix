package com.enablix.core.correlation;

public class CorrelationRuleSource extends SourceMetadata {

	private String correlationRuleId;

	public CorrelationRuleSource(String correlationRuleId, SourceType type) {
		super(type);
		this.correlationRuleId = correlationRuleId;
	}
	
	public String getCorrelationRuleId() {
		return correlationRuleId;
	}

	public void setCorrelationRuleId(String correlationRuleId) {
		this.correlationRuleId = correlationRuleId;
	}
	
}

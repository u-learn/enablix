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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((correlationRuleId == null) ? 0 : correlationRuleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CorrelationRuleSource other = (CorrelationRuleSource) obj;
		if (correlationRuleId == null) {
			if (other.correlationRuleId != null)
				return false;
		} else if (!correlationRuleId.equals(other.correlationRuleId))
			return false;
		return true;
	}
	
	
	
}

package com.enablix.core.domain.content.quality;

public class QualityAlert {

	public enum AlertSeverity {
		LOW, MEDIUM, HIGH
	}
	
	private String ruleId;
	
	private String message;
	
	private AlertLevel level;
	
	private AlertSeverity severity;
	
	private AlertInfo info;

	@SuppressWarnings("unused")
	private QualityAlert() {
		// for ORM
	}

	public QualityAlert(String ruleId, AlertLevel level) {
		this(ruleId, level, AlertSeverity.MEDIUM);
	}
	
	public QualityAlert(String ruleId, AlertLevel level, AlertSeverity severity) {
		super();
		this.ruleId = ruleId;
		this.level = level;
		this.severity = severity;
	}
	

	public String getRuleId() {
		return ruleId;
	}

	public AlertInfo getInfo() {
		return info;
	}

	public void setInfo(AlertInfo info) {
		this.info = info;
	}

	public AlertLevel getLevel() {
		return level;
	}

	public void setLevel(AlertLevel level) {
		this.level = level;
	}
	
	public AlertSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(AlertSeverity severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String alertMessage) {
		this.message = alertMessage;
	}
	
}

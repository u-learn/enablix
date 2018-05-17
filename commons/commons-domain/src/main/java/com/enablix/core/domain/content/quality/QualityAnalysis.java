package com.enablix.core.domain.content.quality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class QualityAnalysis {

	private Collection<QualityAlert> alerts;
	
	private Collection<String> rulesExecuted;
	
	private boolean hasError;
	
	public QualityAnalysis() {
		this.alerts = new ArrayList<>();
		this.rulesExecuted = new HashSet<>();
		this.hasError = false;
	}

	public Collection<QualityAlert> getAlerts() {
		return Collections.unmodifiableCollection(alerts);
	}

	public void addAlert(QualityAlert alert) {
		
		this.alerts.add(alert);
		
		if (alert.getLevel() == AlertLevel.ERROR) {
			hasError = true;
		}
	}
	
	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public boolean hasAlerts() {
		return !alerts.isEmpty();
	}

	public void addAlerts(Collection<QualityAlert> alerts2) {
		alerts2.forEach((alert) -> addAlert(alert));
	}
	
	public Collection<String> getRulesExecuted() {
		return rulesExecuted;
	}
	
	public void addRuleExecuted(String ruleId) {
		this.rulesExecuted.add(ruleId);
	}
	
}

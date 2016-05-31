package com.enablix.core.domain.trigger;

import java.util.Date;

import com.enablix.core.commons.xsdtopojo.CheckpointType;

public class LifecycleCheckpoint<T extends Trigger> {

	public static enum CheckpointStatus {
		STARTED, PENDING, COMPLETED
	}
	
	private T trigger;
	
	private String triggerLifecycleId;
	
	private String triggerLifecycleRuleId;
	
	private CheckpointType checkpointDefinition;
	
	private Date scheduledExecDate;
	
	private Date executedOn;
	
	private CheckpointStatus status = CheckpointStatus.PENDING;
	
	public T getTrigger() {
		return trigger;
	}

	public void setTrigger(T trigger) {
		this.trigger = trigger;
	}

	public String getTriggerLifecycleId() {
		return triggerLifecycleId;
	}

	public void setTriggerLifecycleId(String triggerLifecycleId) {
		this.triggerLifecycleId = triggerLifecycleId;
	}
	
	public String getTriggerLifecycleRuleId() {
		return triggerLifecycleRuleId;
	}

	public void setTriggerLifecycleRuleId(String triggerLifecycleRuleId) {
		this.triggerLifecycleRuleId = triggerLifecycleRuleId;
	}

	public CheckpointType getCheckpointDefinition() {
		return checkpointDefinition;
	}

	public void setCheckpointDefinition(CheckpointType checkpointDefinition) {
		this.checkpointDefinition = checkpointDefinition;
	}

	public Date getScheduledExecDate() {
		return scheduledExecDate;
	}

	public void setScheduledExecDate(Date scheduledExecDate) {
		this.scheduledExecDate = scheduledExecDate;
	}

	public Date getExecutedOn() {
		return executedOn;
	}

	public void setExecutedOn(Date executedOn) {
		this.executedOn = executedOn;
	}

	public CheckpointStatus getStatus() {
		return status;
	}

	public void setStatus(CheckpointStatus status) {
		this.status = status;
	}
	
	public void executionStarted() {
		this.setStatus(CheckpointStatus.STARTED);
	}
	
	public void executionCompleted() {
		this.setStatus(CheckpointStatus.COMPLETED);
	}
	
}

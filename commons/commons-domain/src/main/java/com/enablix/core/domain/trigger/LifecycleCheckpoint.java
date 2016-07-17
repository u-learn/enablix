package com.enablix.core.domain.trigger;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_lifecycle_checkpoint")
public class LifecycleCheckpoint<T extends Trigger> extends BaseDocumentEntity {

	public static enum ExecutionStatus {
		STARTED, PENDING, COMPLETED, FAILED
	}
	
	private T trigger;
	
	private String triggerLifecycleId;
	
	private String triggerLifecycleRuleId;
	
	private CheckpointType checkpointDefinition;
	
	private Date scheduledExecDate;
	
	private Date executedOn;
	
	private ExecutionStatus status;
	
	private Map<Integer, ExecutionStatus> actionStatus;
	
	public LifecycleCheckpoint(T trigger, String triggerLifecycleId, String triggerLifecycleRuleId,
			CheckpointType checkpointDefinition, Date scheduledExecDate) {
		super();
		this.trigger = trigger;
		this.triggerLifecycleId = triggerLifecycleId;
		this.triggerLifecycleRuleId = triggerLifecycleRuleId;
		this.checkpointDefinition = checkpointDefinition;
		this.scheduledExecDate = scheduledExecDate;
		init();
	}

	private void init() {
		this.status = ExecutionStatus.PENDING;
		this.actionStatus = new HashMap<>();
	}
	
	public T getTrigger() {
		return trigger;
	}

	public String getTriggerLifecycleId() {
		return triggerLifecycleId;
	}

	public String getTriggerLifecycleRuleId() {
		return triggerLifecycleRuleId;
	}

	public CheckpointType getCheckpointDefinition() {
		return checkpointDefinition;
	}

	public Date getScheduledExecDate() {
		return scheduledExecDate;
	}

	public Date getExecutedOn() {
		return executedOn;
	}

	public void setExecutedOn(Date executedOn) {
		this.executedOn = executedOn;
	}

	public ExecutionStatus getStatus() {
		return status;
	}

	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}
	
	public void executionStarted() {
		this.setStatus(ExecutionStatus.STARTED);
		this.setExecutedOn(Calendar.getInstance().getTime());
	}
	
	public void executionCompleted() {
		this.setStatus(ExecutionStatus.COMPLETED);
	}

	public void executionFailed() {
		this.setStatus(ExecutionStatus.FAILED);
	}
	
	public void actionExecStarted(int actionOrder) {
		actionStatus.put(actionOrder, ExecutionStatus.STARTED);
	}
	
	public void actionExecCompleted(int actionOrder) {
		actionStatus.put(actionOrder, ExecutionStatus.COMPLETED);
	}
	
	public void actionExecFailed(int actionOrder) {
		actionStatus.put(actionOrder, ExecutionStatus.FAILED);
	}
	
	public ExecutionStatus getActionExecStatus(int actionOrder) {
		ExecutionStatus actionExecStatus = actionStatus.get(actionOrder);
		return actionExecStatus == null ? ExecutionStatus.PENDING : actionExecStatus;
	}

	public void setScheduledExecDate(Date scheduledExecDate) {
		this.scheduledExecDate = scheduledExecDate;
	}

	public void resetActionExec(int actionOrder) {
		actionStatus.put(actionOrder, ExecutionStatus.PENDING);
	}
	
}

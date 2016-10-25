package com.enablix.state.change.model;

import java.util.Date;

public class ActionData<T extends RefObject, I extends ActionInput, R> {
	
	private String actionName;
	
	private String fromState;
	
	private String toState;
	
	private I actionInput;
	
	private R actionResult;
	
	private String actorUserId;
	
	private String actorName;
	
	private Date actionDate;

	public ActionData(String actionName, String fromState, String toState, I actionInput, 
			R actionResult, String actorUserId, String actorName, Date actionDate) {
		super();
		this.actionName = actionName;
		this.fromState = fromState;
		this.toState = toState;
		this.actionInput = actionInput;
		this.actionResult = actionResult;
		this.actorUserId = actorUserId;
		this.actorName = actorName;
		this.actionDate = actionDate;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public String getFromState() {
		return fromState;
	}

	public void setFromState(String fromState) {
		this.fromState = fromState;
	}

	public String getToState() {
		return toState;
	}

	public void setToState(String toState) {
		this.toState = toState;
	}

	public I getActionInput() {
		return actionInput;
	}

	public void setActionInput(I actionInput) {
		this.actionInput = actionInput;
	}

	public R getActionResult() {
		return actionResult;
	}

	public void setActionResult(R actionResult) {
		this.actionResult = actionResult;
	}

	public String getActorUserId() {
		return actorUserId;
	}

	public void setActorUserId(String actorIdentity) {
		this.actorUserId = actorIdentity;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	
}

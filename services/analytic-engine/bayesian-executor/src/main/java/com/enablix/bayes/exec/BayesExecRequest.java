package com.enablix.bayes.exec;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class BayesExecRequest {
	
	public enum RecorderType {
		CSV, DB
	}
	
	private List<String> forTenants;
	
	private List<String> forUsers;
	
	private Date runAsStartDate;
	
	private Date runAsEndDate;
	
	private RecorderType recorderType = RecorderType.CSV;
	
	public Date getRunAsStartDate() {
		return runAsStartDate;
	}

	@DateTimeFormat(iso = ISO.DATE)
	public void setRunAsStartDate(Date runAsDate) {
		this.runAsStartDate = runAsDate;
	}
	
	public Date getRunAsEndDate() {
		return runAsEndDate;
	}

	@DateTimeFormat(iso = ISO.DATE)
	public void setRunAsEndDate(Date runAsEndDate) {
		this.runAsEndDate = runAsEndDate;
	}

	public List<String> getForTenants() {
		return forTenants;
	}
	
	public void setForTenants(List<String> forTenants) {
		this.forTenants = forTenants;
	}
	
	public List<String> getForUsers() {
		return forUsers;
	}
	
	public void setForUsers(List<String> forUsers) {
		this.forUsers = forUsers;
	}

	public RecorderType getRecorderType() {
		return recorderType;
	}

	public void setRecorderType(RecorderType recorderType) {
		this.recorderType = recorderType;
	}

	@Override
	public String toString() {
		return "BayesExecRequest [forTenants=" + forTenants + ", forUsers=" + forUsers + "]";
	}
	
}

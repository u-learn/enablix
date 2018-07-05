package com.enablix.app.order;

import com.enablix.core.api.SearchRequest;

public class ReorderRequest {

	private String collectionName;
	private String recordIdentity;
	private int currentOrder;
	private int movement;
	
	private SearchRequest updateRecordFilter;
	
	public String getCollectionName() {
		return collectionName;
	}
	
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
	public String getRecordIdentity() {
		return recordIdentity;
	}
	
	public void setRecordIdentity(String recordIdentity) {
		this.recordIdentity = recordIdentity;
	}
	
	public int getCurrentOrder() {
		return currentOrder;
	}
	
	public void setCurrentOrder(int currentOrder) {
		this.currentOrder = currentOrder;
	}
	
	public int getMovement() {
		return movement;
	}
	
	public void setMovement(int movement) {
		this.movement = movement;
	}

	public SearchRequest getUpdateRecordFilter() {
		return updateRecordFilter;
	}

	public void setUpdateRecordFilter(SearchRequest updateRecordFilter) {
		this.updateRecordFilter = updateRecordFilter;
	}
	
}

package com.enablix.app.order;

public class SwapOrderRequest {

	private String collectionName;
	
	private String record1Identity;
	
	private String record2Identity;
	
	private int record1NewOrder;
	
	private int record2NewOrder;

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getRecord1Identity() {
		return record1Identity;
	}

	public void setRecord1Identity(String record1Identity) {
		this.record1Identity = record1Identity;
	}

	public String getRecord2Identity() {
		return record2Identity;
	}

	public void setRecord2Identity(String record2Identity) {
		this.record2Identity = record2Identity;
	}

	public int getRecord1NewOrder() {
		return record1NewOrder;
	}

	public void setRecord1NewOrder(int record1NewOrder) {
		this.record1NewOrder = record1NewOrder;
	}

	public int getRecord2NewOrder() {
		return record2NewOrder;
	}

	public void setRecord2NewOrder(int record2NewOrder) {
		this.record2NewOrder = record2NewOrder;
	}
	
}

package com.enablix.core.domain.report.attribution;

import java.util.List;

public class Attribution {

	private String name;
	
	private Double value;
	
	private Long count;
	
	private List<AttributionItem> attributionItems;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<AttributionItem> getAttributionItems() {
		return attributionItems;
	}

	public void setAttributionItems(List<AttributionItem> attributionItem) {
		this.attributionItems = attributionItem;
	}
	
}

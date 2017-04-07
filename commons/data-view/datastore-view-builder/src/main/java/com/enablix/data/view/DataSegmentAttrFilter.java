package com.enablix.data.view;

import com.enablix.core.domain.segment.DataSegmentAttribute;

public class DataSegmentAttrFilter {

	private DataSegmentAttribute dataSegmentAttribute;
	
	private String recordAttributeId;
	
	public DataSegmentAttrFilter(DataSegmentAttribute dataSegmentAttribute, String recordAttributeId) {
		super();
		this.dataSegmentAttribute = dataSegmentAttribute;
		this.recordAttributeId = recordAttributeId;
	}

	public DataSegmentAttribute getDataSegmentAttribute() {
		return dataSegmentAttribute;
	}

	public void setDataSegmentAttribute(DataSegmentAttribute dataSegmentAttribute) {
		this.dataSegmentAttribute = dataSegmentAttribute;
	}

	public String getRecordAttributeId() {
		return recordAttributeId;
	}

	public void setRecordAttributeId(String recordAttributeId) {
		this.recordAttributeId = recordAttributeId;
	}

	public Object getAttributeValue() {
		return dataSegmentAttribute.getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSegmentAttribute == null) ? 0 : dataSegmentAttribute.hashCode());
		result = prime * result + ((recordAttributeId == null) ? 0 : recordAttributeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSegmentAttrFilter other = (DataSegmentAttrFilter) obj;
		if (dataSegmentAttribute == null) {
			if (other.dataSegmentAttribute != null)
				return false;
		} else if (!dataSegmentAttribute.equals(other.dataSegmentAttribute))
			return false;
		if (recordAttributeId == null) {
			if (other.recordAttributeId != null)
				return false;
		} else if (!recordAttributeId.equals(other.recordAttributeId))
			return false;
		return true;
	}
	
	

}

package com.enablix.core.domain.content.summary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_content_coverage")
public class ContentCoverage extends BaseDocumentEntity {
	
	private String contentQId;
	
	private String containerLabel;
	
	private String recordTitle;
	
	private String recordIdentity;
	
	private Date recordCreatedAt;
	
	private String recordCreatedBy;
	
	private String recordCreatedByName;
	
	private Date recordModifiedAt;
	
	private String recordModifiedBy;
	
	private String recordModifiedByName;
	
	private boolean archived;
	
	private Date recordArchivedAt;
	
	private String recordArchivedBy;
	
	private String recordArchivedByName;
	
	private List<CoverageStat> stats;
	
	private Date asOfDate;
	
	private boolean latest;
	
	public ContentCoverage() {
		this.stats = new ArrayList<>();
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}
	
	public String getContainerLabel() {
		return containerLabel;
	}

	public void setContainerLabel(String containerLabel) {
		this.containerLabel = containerLabel;
	}

	public String getRecordTitle() {
		return recordTitle;
	}

	public void setRecordTitle(String recordTitle) {
		this.recordTitle = recordTitle;
	}

	public String getRecordIdentity() {
		return recordIdentity;
	}

	public void setRecordIdentity(String recordIdentity) {
		this.recordIdentity = recordIdentity;
	}

	public List<CoverageStat> getStats() {
		return stats;
	}

	public void setStats(List<CoverageStat> stats) {
		this.stats = stats;
	}

	public void addCoverageStat(String property, long count) {
		
		CoverageStat stat = new CoverageStat();
		stat.setItemId(property);
		stat.setCount(count);
		
		stats.add(stat);
	}

	public Date getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}

	public boolean isLatest() {
		return latest;
	}

	public void setLatest(boolean latest) {
		this.latest = latest;
	}
	
	public Date getRecordCreatedAt() {
		return recordCreatedAt;
	}

	public void setRecordCreatedAt(Date recordCreatedAt) {
		this.recordCreatedAt = recordCreatedAt;
	}

	public String getRecordCreatedBy() {
		return recordCreatedBy;
	}

	public void setRecordCreatedBy(String recordCreatedBy) {
		this.recordCreatedBy = recordCreatedBy;
	}

	public String getRecordCreatedByName() {
		return recordCreatedByName;
	}

	public void setRecordCreatedByName(String recordCreatedByName) {
		this.recordCreatedByName = recordCreatedByName;
	}

	public Date getRecordModifiedAt() {
		return recordModifiedAt;
	}

	public void setRecordModifiedAt(Date recordModifiedAt) {
		this.recordModifiedAt = recordModifiedAt;
	}

	public String getRecordModifiedBy() {
		return recordModifiedBy;
	}

	public void setRecordModifiedBy(String recordModifiedBy) {
		this.recordModifiedBy = recordModifiedBy;
	}

	public String getRecordModifiedByName() {
		return recordModifiedByName;
	}

	public void setRecordModifiedByName(String recordModifiedByName) {
		this.recordModifiedByName = recordModifiedByName;
	}
	
	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public Date getRecordArchivedAt() {
		return recordArchivedAt;
	}

	public void setRecordArchivedAt(Date recordArchivedAt) {
		this.recordArchivedAt = recordArchivedAt;
	}

	public String getRecordArchivedBy() {
		return recordArchivedBy;
	}

	public void setRecordArchivedBy(String recordArchivedBy) {
		this.recordArchivedBy = recordArchivedBy;
	}

	public String getRecordArchivedByName() {
		return recordArchivedByName;
	}

	public void setRecordArchivedByName(String recordArchivedByName) {
		this.recordArchivedByName = recordArchivedByName;
	}

	@Override
	public String toString() {
		return "ContentCoverage [contentQId=" + contentQId + ", recordTitle=" + recordTitle + ", recordIdentity="
				+ recordIdentity + ", stats=" + stats + ", asOfDate=" + asOfDate + ", latest=" + latest
				+ "]";
	}
	
	public static class CoverageStat {
		
		private String itemId;
		private long count;
		
		public String getItemId() {
			return itemId;
		}
		
		public void setItemId(String itemId) {
			this.itemId = itemId;
		}
		
		public long getCount() {
			return count;
		}
		
		public void setCount(long count) {
			this.count = count;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
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
			CoverageStat other = (CoverageStat) obj;
			if (itemId == null) {
				if (other.itemId != null)
					return false;
			} else if (!itemId.equals(other.itemId))
				return false;
			return true;
		}
		
		
	}

}

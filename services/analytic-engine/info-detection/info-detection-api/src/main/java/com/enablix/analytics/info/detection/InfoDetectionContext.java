package com.enablix.analytics.info.detection;

public class InfoDetectionContext {

	private Information information;
	
	private Assessment assessment;
	
	private boolean saveContentRecord;

	public InfoDetectionContext(Information information, Assessment assessment) {
		this.information = information;
		this.assessment = assessment;
	}
	
	public Information getInformation() {
		return information;
	}

	public Assessment getAssessment() {
		return assessment;
	}

	public boolean isSaveContentRecord() {
		return saveContentRecord;
	}

	public void setSaveContentRecord(boolean saveContentRecord) {
		this.saveContentRecord = saveContentRecord;
	}
	
}

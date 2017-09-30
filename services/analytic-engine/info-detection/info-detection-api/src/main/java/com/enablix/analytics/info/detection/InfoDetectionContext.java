package com.enablix.analytics.info.detection;

public class InfoDetectionContext {

	private Information information;
	
	private Assessment assessment;
	
	private boolean saveContentRecord;
	
	private InfoDetectionConfiguration infoDetectionConfig;

	public InfoDetectionContext(Information information, Assessment assessment, InfoDetectionConfiguration infoDetectionConfig) {
		this.information = information;
		this.assessment = assessment;
		this.infoDetectionConfig = infoDetectionConfig;
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

	public InfoDetectionConfiguration getInfoDetectionConfig() {
		return infoDetectionConfig;
	}
	
}

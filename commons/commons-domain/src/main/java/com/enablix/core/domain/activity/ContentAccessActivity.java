package com.enablix.core.domain.activity;

public class ContentAccessActivity extends ContentActivity {

	private String campaign; // if part of some campaign
	
	private String campaignId; // campaign id
	
	public ContentAccessActivity(String itemIdentity, String containerQId, ContainerType containerType) {
		this(itemIdentity, containerQId, containerType, null, null);
	}

	public ContentAccessActivity(String itemIdentity, String containerQId, ContainerType containerType,
			String campaign, String campaignId) {

		super(itemIdentity, containerQId, containerType, ContentActivityType.CONTENT_ACCESS);
		
		this.campaign = campaign;
		this.campaignId = campaignId;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

}

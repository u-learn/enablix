package com.enablix.slack.integration.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlackTeamDtls {
	
	@JsonProperty("team_name")
	private String teamName;
	
	@JsonProperty("access_token")
	private String accessToken;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}

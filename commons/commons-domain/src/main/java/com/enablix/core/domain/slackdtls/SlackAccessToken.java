package com.enablix.core.domain.slackdtls;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.BaseDocumentEntity;
@Document(collection = AppConstants.SYSTEM_SLACK_TOKEN)
public class SlackAccessToken extends BaseDocumentEntity{
	String userID;
	String accessToken;
	String teamName;
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public SlackAccessToken(String userID,String accessToken,String teamName) {
		this.userID=userID;
		this.accessToken = accessToken;
		this.teamName=teamName;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
}
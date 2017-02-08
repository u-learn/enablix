package com.enablix.app.slack.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlackChannels {
	
	@JsonProperty("channels")
    private List<SlackChannelDtls> slackChannels;

	public List<SlackChannelDtls> getSlackChannels() {
		return slackChannels;
	}

	public void setSlackChannels(List<SlackChannelDtls> slackChannels) {
		this.slackChannels = slackChannels;
	}
}

package com.enablix.core.domain.activity;

public class ActivityChannel {

	public enum Channel {
		WEB, EMAIL, EXTERNAL
	}
	
	private Channel channel;

	public ActivityChannel(Channel channel) {
		this.channel = channel;
	}
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}

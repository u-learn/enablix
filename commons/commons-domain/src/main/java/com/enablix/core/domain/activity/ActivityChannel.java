package com.enablix.core.domain.activity;

public class ActivityChannel<CI> {

	public enum Channel {
		WEB, EMAIL, EXTERNAL
	}
	
	private Channel channel;
	
	private CI channelInfo;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public CI getChannelInfo() {
		return channelInfo;
	}

	public void setChannelInfo(CI channelInfo) {
		this.channelInfo = channelInfo;
	}
	
}

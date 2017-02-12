package com.enablix.core.domain.activity;

import com.enablix.commons.util.StringUtil;

public class ActivityChannel {

	public enum Channel {
		
		WEB, EMAIL, EXTERNAL,EMAILCLIENT,SLACK;
		
		public static Channel parse(String channel) {
			
			if (!StringUtil.isEmpty(channel)) {
			
				for (Channel ch : Channel.values()) {
					if (ch.toString().equals(channel)) {
						return ch;
					}
				}
			}
			
			return null;
		}
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

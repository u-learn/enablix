package com.enablix.app.content.ui;

import java.util.HashMap;
import java.util.Map;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.TextLinkifier.LinkDecorator;
import com.enablix.core.activity.audit.ActivityTrackingConstants;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.ui.ContentPreviewInfo.PreviewProperty;

public class DisplayContext {
	
	private LinkDecorator linkDecorator;
	
	private Channel displayChannel;
	
	private Map<String, String> trackingParams;
	
	private PreviewProperty previewProperty;

	public Map<String, String> getTrackingParams() {
		return trackingParams;
	}

	public void setTrackingParams(Map<String, String> trackingParams) {
		this.trackingParams = trackingParams;
	}

	public LinkDecorator getLinkDecorator() {
		return linkDecorator;
	}

	public void setLinkDecorator(LinkDecorator linkDecorator) {
		this.linkDecorator = linkDecorator;
	}
	
	public String appendTrackingParams(String url) {
		
		String params = trackingParamsString();
		
		if (StringUtil.hasText(params)) {
			return url + "?" + params;
		}
		
		return url;
	}
	
	public String trackingParamsString() {
		
		String paramsString = null;
		
		Map<String, String> allParams = new HashMap<>();
		if (displayChannel != null) {
			allParams.put(ActivityTrackingConstants.ACTIVITY_CHANNEL, displayChannel.name());
		}
		
		if (trackingParams != null && !trackingParams.isEmpty()) {
			allParams.putAll(trackingParams);
		}
		
		if (!allParams.isEmpty()) {
			
			StringBuilder params = new StringBuilder();
			
			boolean first = true;
			
			for (Map.Entry<String, String> entry : allParams.entrySet()) {
				
				if (first) { 
					first = false;
				} else {
					params.append("&");
				}
				
				params.append(entry.getKey()).append("=").append(entry.getValue());
			}
			
			paramsString = params.toString();
		}
		
		return paramsString;
	}

	public Channel getDisplayChannel() {
		return displayChannel;
	}

	public void setDisplayChannel(Channel displayChannel) {
		this.displayChannel = displayChannel;
	}

	public PreviewProperty getPreviewProperty() {
		return previewProperty;
	}

	public void setPreviewProperty(PreviewProperty previewProperty) {
		this.previewProperty = previewProperty;
	}

}

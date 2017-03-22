package com.enablix.dms.sharepoint.online;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.dms.sharepoint.service.SharepointSession;
import com.google.common.base.Joiner;

public class SharepointOnlineSession implements SharepointSession {

	private String siteUrl;
	
	private String endpointUrl;
	
	private String securityToken;
	
	private List<String> cookies;
	
	private String formDigest;

	private String baseFolder;

	public SharepointOnlineSession(String siteUrl, String endpointUrl, String baseFolder,
			String securityToken, List<String> cookies, String formDigest) {
		
		super();
		
		this.siteUrl = siteUrl;
		this.endpointUrl = endpointUrl;
		this.baseFolder = baseFolder;
		this.securityToken = securityToken;
		this.cookies = cookies;
		this.formDigest = formDigest;
	}
	
	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public List<String> getCookies() {
		return cookies;
	}

	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}

	public String getFormDigest() {
		return formDigest;
	}

	public void setFormDigest(String formDigest) {
		this.formDigest = formDigest;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	@Override
	public Map<String, String> getRequestHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Cookie", Joiner.on(';').join(cookies));
		headers.put("X-RequestDigest", formDigest);
		return headers;
	}

	@Override
	public String getSiteUrl() {
		return siteUrl;
	}

	@Override
	public String getEndpointUrl() {
		return endpointUrl;
	}

	public String getBaseFolder() {
		return baseFolder;
	}

	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}
	
}

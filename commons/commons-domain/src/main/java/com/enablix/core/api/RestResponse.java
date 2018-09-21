package com.enablix.core.api;

public class RestResponse {
	
	private static RestResponse SUCCESS = new RestResponse("success");
	private static RestResponse FAILURE = new RestResponse("failed");
	
	private String result;
	
	private RestResponse(String result) {
		this.result = result;
	}
	
	public static RestResponse success() {
		return SUCCESS;
	}
	
	public static RestResponse failure() {
		return FAILURE;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
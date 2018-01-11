package com.enablix.core.api;

public class WebPortal {

	private String name;
	
	private WebPortal(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static WebPortal V1 = new WebPortal("v1");
	public static WebPortal V2 = new WebPortal("v2");

	public static WebPortal defaultPortal() {
		return V1;
	}
	
}

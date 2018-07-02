package com.enablix.ms.graph;

import com.enablix.ms.graph.impl.MSGraphSDKImpl;

public class MSGraphSDKFactory {

	public static MSGraphSDK createSDK() {
		return new MSGraphSDKImpl();
	}
	
}

package com.enablix.app.main;

public class AssetUtil {

	public static final String UI_ASSETS_FOLDER_NAME = "ui-assets";
	public static String uiAssetRootPath;
	
	public static void setUiAssetRootPath(String assetRootPath){
		uiAssetRootPath = assetRootPath;
	}
	
	public static String uiAssetRootPath(){
		return uiAssetRootPath;
	}
}

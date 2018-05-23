package com.enablix.commons.util;

import java.util.Arrays;

public class FileUtil {

	public static String[] getFolderList(String folderHierarchy) {
		folderHierarchy = StringUtil.trimCharacter(folderHierarchy, '/');
		return folderHierarchy.split("/");
	}
	
	public static String getFileExt(String filename) {
		
		String[] split = filename.split("\\.");
	    
		if (split.length == 1 || ( split[0] == "" && split.length == 2 )) {
	        return null;
	    }
	    
	    return split[split.length - 1].toLowerCase();
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(getFolderList("/Enablix/Case Study/AML/")));
		System.out.println(Arrays.toString(getFolderList("//Enablix/Case Study/AML//")));
		System.out.println(Arrays.toString(getFolderList("//Enablix/Case Study/AML")));
		System.out.println(Arrays.toString(getFolderList("Enablix/Case Study/AML//")));
		
		System.out.println(getFileExt("Abc.pdf"));
	}
	
}

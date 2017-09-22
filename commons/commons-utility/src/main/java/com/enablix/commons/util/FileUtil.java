package com.enablix.commons.util;

import java.util.Arrays;

public class FileUtil {

	public static String[] getFolderList(String folderHierarchy) {
		folderHierarchy = StringUtil.trimCharacter(folderHierarchy, '/');
		return folderHierarchy.split("/");
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(getFolderList("/Enablix/Case Study/AML/")));
		System.out.println(Arrays.toString(getFolderList("//Enablix/Case Study/AML//")));
		System.out.println(Arrays.toString(getFolderList("//Enablix/Case Study/AML")));
		System.out.println(Arrays.toString(getFolderList("Enablix/Case Study/AML//")));
	}
	
}

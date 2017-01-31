package com.enablix.core.web.versioning;

public interface VersionedResource {

	/**
	 * Returns the name of the resource
	 * @return String - the name of the resource
	 */
	String getResourceName();
	
	/**
	 * Returns the current version of the resource
	 * @return String - the current version of the resource
	 */
	String getResourceVersion();
	
	/**
	 * Checks if the current version of the resource on the server side is
	 * compatible with the given {@code version}
	 * 
	 * @param version Compare the current version with this version
	 * @return boolean - {@code true} if the given version is compatible with the current
	 * 					 of the resource, otherwise {@code false}
	 */
	boolean isVersionCompatible(String version);
	
}

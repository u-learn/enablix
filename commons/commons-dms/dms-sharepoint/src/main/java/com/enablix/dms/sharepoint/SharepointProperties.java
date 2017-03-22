package com.enablix.dms.sharepoint;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sharepoint.integration")
public class SharepointProperties {

	/*
	 * example url:
	 * 
	 * https://enablix.sharepoint.com/sites/enablix-team/_api/Web/GetFolderByServerRelativeUrl('/sites/enablix-team/Shared%20Documents/Dikshit%20Local%20Env')
	 * 
	 */
	private String getFolderByServerRelativePathUrl = "${0}/_api/Web/GetFolderByServerRelativeUrl('${1}')";
	
	/*
	 * example json:
	 * 
	 * { '__metadata': { 'type': 'SP.Folder' }, 'ServerRelativeUrl': '/Shared%20Documents/Dikshit%20Local%20Env'}
	 * 
	 * Adding extra single-quote around { so that it can be escaped during message formatting
	 */
	private String createFolderJson = "{ '__metadata': { 'type': 'SP.Folder' }, 'ServerRelativeUrl': '${0}' }";
	
	/*
	 * example uri:
	 * 
	 * https://enablix.sharepoint.com/sites/enablix-team/_api/web/folders
	 * 
	 */
	private String createFolderUri = "${0}/_api/web/folders";
	
	/*
	 * example uri:
	 * 
	 * https://enablix.sharepoint.com/_api/Web/GetFolderByServerRelativeUrl('/Shared%20Documents/Dikshit%20Local%20Env')/Files/add(url='Brief.pdf',overwrite=true)
	 * 
	 * OR
	 * 
	 * https://enablix.sharepoint.com/sites/enablix-team/_api/Web/GetFolderByServerRelativeUrl('/sites/enablix-team/Shared%20Documents/Dikshit%20Local%20Env')/Files/add(url='Brief.pdf',overwrite=true)
	 * 
	 */
	private String postFileUri = "${0}/_api/Web/GetFolderByServerRelativeUrl('${1}')/Files/add(url='${2}',overwrite=true)";
	
	/*
	 * example uri:
	 * 
	 * https://enablix.sharepoint.com/_api/Web/GetFileByServerRelativeUrl('/Shared%20Documents/Dikshit%20Local%20Env/Brief.pdf')/$value
	 * 
	 */
	private String downloadFileUri = "${0}/_api/Web/GetFileByServerRelativeUrl('${1}')/$value";
	
	/*
	 * example uri:
	 * 
	 * https://enablix.sharepoint.com/_api/web/GetFileByServerRelativeUrl('/Shared%20Documents/Dikshit%20Local%20Env/Brief.pdf')/moveto(newurl='/Shared%20Documents/Dikshit%20Test/Brief.pdf',flags=1)
	 */
	private String moveFileUri = "${0}/_api/web/GetFileByServerRelativeUrl('${1}')/moveto(newurl='${2}',flags=1)";
	
	/*
	 * example uri:
	 * 
	 * https://enablix.sharepoint.com/_api/Web/GetFileByServerRelativeUrl('/Shared%20Documents/Dikshit%20Local%20Env/Brief.pdf')
	 * 
	 * This uri is similar to get file uri. The headers are set different for delete operation
	 */
	private String deleteFileUri = "${0}/_api/Web/GetFileByServerRelativeUrl('${1}')";
	
	
	public String getGetFolderByServerRelativePathUrl() {
		return getFolderByServerRelativePathUrl;
	}

	public void setGetFolderByServerRelativePathUrl(String getFolderByServerRelativePathUrl) {
		this.getFolderByServerRelativePathUrl = getFolderByServerRelativePathUrl;
	}

	public String getCreateFolderJson() {
		return createFolderJson;
	}

	public void setCreateFolderJson(String createFolderJson) {
		this.createFolderJson = createFolderJson;
	}

	public String getCreateFolderUri() {
		return createFolderUri;
	}

	public void setCreateFolderUri(String createFolderUri) {
		this.createFolderUri = createFolderUri;
	}

	public String getPostFileUri() {
		return postFileUri;
	}

	public void setPostFileUri(String postFileUri) {
		this.postFileUri = postFileUri;
	}

	public String getDownloadFileUri() {
		return downloadFileUri;
	}

	public void setDownloadFileUri(String downloadFileUri) {
		this.downloadFileUri = downloadFileUri;
	}

	public String getMoveFileUri() {
		return moveFileUri;
	}

	public void setMoveFileUri(String moveFileUri) {
		this.moveFileUri = moveFileUri;
	}

	public String getDeleteFileUri() {
		return deleteFileUri;
	}

	public void setDeleteFileUri(String deleteFileUri) {
		this.deleteFileUri = deleteFileUri;
	}

}

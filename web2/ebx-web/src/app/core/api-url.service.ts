import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable()
export class ApiUrlService {

  static GET_USER = "/user";
  static GET_DEFAULT_CONTENT_TEMPLATE = "/template/default";
  static GET_ALL_RESOURCE_VERSIONS = "/version/all/";
  static GET_CONTENT_RECORD = "/data/t/:templateId/c/:contentQId/d/:recordIdentity";
  static GET_EMBED_INFO = "/urlembed/fetch/";
  static GET_EXT_LINK_URL = "/xlink?u=:url";
  static GET_BOUNDED_REF_LIST = "/bounded/list/:templateId/:contentItemQId/";
  static GET_DOC_PREVIEW_DATA = "/doc/pd/:docIdentity";
  static GET_RECORD_AND_CHILDREN = "/data/rnc/c/:contentQId/r/:contentIdentity";
  static GET_TENANT = "/tenant/fetch";
  static GET_DELETE_CONTENT_RECORD = "/content/delete/t/:templateId/c/:contentQId/r/:recordIdentity";

  static POST_FOR_DATA_SEARCH = "/data/search/t/:domainType/";
  static POST_FOR_CONTAINER_DATA_SEARCH = "/data/search/cq/:containerQId/";

  static POST_INSERT_ROOT_CONTAINER_DATA = "/content/update/t/:templateId/c/:contentQId/";
  static POST_UPDATE_CONTAINER_DATA = "/content/update/t/:templateId/c/:contentQId/";
  static POST_INSERT_CHILD_CONTAINER_DATA = "/content/update/t/:templateId/c/:contentQId/r/:parentIdentity";
  static POST_BOUNDED_DEF_REF_LIST = "/bounded/d/list/";
  static POST_FILE_UPLOAD = '/doc/upload';

  constructor() { }

  getAPIUrl(url: string, urlPlaceholders?: { [key: string] : string }) : string {

  	let finalUrl = environment.baseAPIUrl + url;
  	
  	if (urlPlaceholders != null) {
  	  for (const prop in urlPlaceholders) {
  	  	finalUrl = finalUrl.replace(":" + prop, urlPlaceholders[prop]);
  	  }
  	}

  	console.log("Endpoint URL: " + finalUrl);
  	return finalUrl;
  }

  getUserUrl() : string {
  	return this.getAPIUrl(ApiUrlService.GET_USER);
  }

  getDefaultContentTemplateUrl() : string {
    return this.getAPIUrl(ApiUrlService.GET_DEFAULT_CONTENT_TEMPLATE);
  }

  postDataSearchUrl(domainType: string) : string {
    return this.getAPIUrl(ApiUrlService.POST_FOR_DATA_SEARCH, {domainType: domainType});
  }

  postContainerDataSearchUrl(containerQId: string) : string {
    return this.getAPIUrl(ApiUrlService.POST_FOR_CONTAINER_DATA_SEARCH, {containerQId: containerQId}); 
  }

  postInsertRootContainerDataUrl(templateId: string, containerQId: string) : string {
    return this.getAPIUrl(ApiUrlService.POST_INSERT_ROOT_CONTAINER_DATA, 
                { templateId: templateId, contentQId: containerQId});
  }

  postInsertChildContainerDataUrl(templateId: string, containerQId: string, parentIdentity: string) : string {
    return this.getAPIUrl(ApiUrlService.POST_INSERT_CHILD_CONTAINER_DATA, 
                { templateId: templateId, contentQId: containerQId, parentIdentity: parentIdentity});
  }

  postUpdateContainerDataUrl(templateId: string, containerQId: string) : string {
    return this.getAPIUrl(ApiUrlService.POST_UPDATE_CONTAINER_DATA, 
                { templateId: templateId, contentQId: containerQId});
  }

  getAllResourceVersions() : string {
    return this.getAPIUrl(ApiUrlService.GET_ALL_RESOURCE_VERSIONS);
  }

  getContentRecordUrl(templateId: string, containerQId: string, contentIdentity: string): string {
    return this.getAPIUrl(ApiUrlService.GET_CONTENT_RECORD, {
                  templateId: templateId,
                  contentQId: containerQId,
                  recordIdentity: contentIdentity
                });
  }

  getEmbedInfoUrl() : string {
    return this.getAPIUrl(ApiUrlService.GET_EMBED_INFO);
  }

  getExtLinkUrl(url: string) : string {
    return this.getAPIUrl(ApiUrlService.GET_EXT_LINK_URL, { url: url });
  }

  getBoundedRefListUrl(templateId: string, contentItemQId) : string {
    return this.getAPIUrl(ApiUrlService.GET_BOUNDED_REF_LIST, 
      {templateId: templateId, contentItemQId: contentItemQId});
  }

  postBoundedDefRefListUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_BOUNDED_DEF_REF_LIST);
  }

  postFileUploadUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_FILE_UPLOAD);
  }

  getDocPreviewDataUrl(docIdentity: string) : string {
    return this.getAPIUrl(ApiUrlService.GET_DOC_PREVIEW_DATA, { docIdentity: docIdentity });
  }

  getRecordAndChildren(contentQId: string, contentIdentity: string) : string {
    return this.getAPIUrl(ApiUrlService.GET_RECORD_AND_CHILDREN, {
      contentQId: contentQId, contentIdentity: contentIdentity
    })
  }

  getTenantUrl() : string {
    return this.getAPIUrl(ApiUrlService.GET_TENANT);
  }

  getContentDeleteUrl(templateId: string, contentQId: string, recordIdentity: string) : string {
    return this.getAPIUrl(ApiUrlService.GET_DELETE_CONTENT_RECORD, {
      templateId: templateId, contentQId: contentQId, recordIdentity: recordIdentity
    });
  }

}

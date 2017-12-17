import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable()
export class ApiUrlService {

  static GET_USER = "/user";
  static GET_DEFAULT_CONTENT_TEMPLATE = "/template/default";
  static GET_ALL_RESOURCE_VERSIONS = "/version/all/";
  static GET_CONTENT_RECORD = "/data/t/:templateId/c/:contentQId/d/:recordIdentity";
  static GET_EMBED_INFO = "/urlembed/fetch/";
  static GET_EXT_LINK_URL = "/xlink?u=:url&cId=:cId&cQId=:cQId";
  static GET_BOUNDED_REF_LIST = "/bounded/list/:templateId/:contentItemQId/";
  static GET_DOC_PREVIEW_DATA = "/doc/pd/:docIdentity";
  static GET_RECORD_AND_CHILDREN = "/data/rnc/c/:contentQId/r/:contentIdentity";
  static GET_TENANT = "/tenant/fetch";
  static GET_DELETE_CONTENT_RECORD = "/content/delete/t/:templateId/c/:contentQId/r/:recordIdentity";
  static GET_CONTENT_REQUEST = "/contentwf/r/:refObjectIdentity/";
  static GET_ALL_SYSTEM_ROLES = "/roles";
  static GET_MEMBER_PROFILE = "/d/user/:userIdentity/"
  static GET_DEFAULT_DOCSTORE_CONFIG = "/docstore/default";
  static GET_CONFIG_INFO = "/config/:configKey/";

  static POST_FOR_DATA_SEARCH = "/data/search/t/:domainType/";
  static POST_FOR_CONTAINER_DATA_SEARCH = "/data/search/cq/:containerQId/";

  static POST_INSERT_ROOT_CONTAINER_DATA = "/content/update/t/:templateId/c/:contentQId/";
  static POST_UPDATE_CONTAINER_DATA = "/content/update/t/:templateId/c/:contentQId/";
  static POST_INSERT_CHILD_CONTAINER_DATA = "/content/update/t/:templateId/c/:contentQId/r/:parentIdentity";
  static POST_BOUNDED_DEF_REF_LIST = "/bounded/d/list/";
  static POST_FILE_UPLOAD = '/doc/upload';
  static POST_BIZ_CONTENT_SEARCH = '/search/bizcontent/';
  
  static POST_SAVE_CONTENT_DRAFT = '/contentwf/savedraft/';
  static POST_DISCARD_CONTENT_REQUEST = "/contentwf/discard/";
  static POST_PUBLISH_CONTENT_REQUEST = "/contentwf/publish/";
  static POST_APPROVE_CONTENT_REQUEST = "/contentwf/approve/";
  static POST_REJECT_CONTENT_REQUEST = "/contentwf/reject/";
  static POST_WITHDRAW_CONTENT_REQUEST = "/contentwf/withdraw/";
  static POST_EDIT_CONTENT_REQUEST = "/contentwf/edit/";
  
  static POST_WITHDRAW_CONTENT_REQUEST_LIST = "/contentwf/withdraw/list/";
  static POST_DISCARD_CONTENT_REQUEST_LIST = "/contentwf/discard/list/";
  static POST_PUBLISH_CONTENT_REQUEST_LIST = "/contentwf/publish/list/";
  static POST_APPROVE_CONTENT_REQUEST_LIST = "/contentwf/approve/list/";
  static POST_REJECT_CONTENT_REQUEST_LIST = "/contentwf/reject/list/";

  static POST_CHECK_USER_EXIST = "/checkuserexist";
  static POST_ADD_MEMBER = "/member/add";
  static POST_UPDATE_MEMBER = "/member/update";
  static POST_DELETE_MEMBERS = "/deletesystemusers";

  static POST_SAVE_DOCSTORE_CONFIG = "/docstore/config";
  static POST_SAVE_CONFIG_INFO = "/config/save";
  static POST_DELETE_CONFIG_INFO = "/config/delete/:configIdentity/"
 
  static GET_LOGOUT = "/logout";

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

  getLogoutUrl() : string {
    return this.getAPIUrl(ApiUrlService.GET_LOGOUT); 
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

  getExtLinkUrl(url: string, cIdentity: string, cQId: string) : string {
    return this.getAPIUrl(ApiUrlService.GET_EXT_LINK_URL, { url: url, cId: cIdentity, cQId: cQId });
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

  getContentRequestUrl(crIdentity: string) : string {
    return this.getAPIUrl(ApiUrlService.GET_CONTENT_REQUEST, {
      refObjectIdentity: crIdentity
    });
  }

  getAllSystemRolesUrl() : string {
    return this.getAPIUrl(ApiUrlService.GET_ALL_SYSTEM_ROLES);
  }

  getMemberProfileUrl(profileIdentity: string) : string {
    return this.getAPIUrl(ApiUrlService.GET_MEMBER_PROFILE, { userIdentity: profileIdentity });
  }

  getDefaultDocstoreConfigUrl() : string {
    return this.getAPIUrl(ApiUrlService.GET_DEFAULT_DOCSTORE_CONFIG);
  }

  getConfigInfoUrl(configKey: string) {
    return this.getAPIUrl(ApiUrlService.GET_CONFIG_INFO, { configKey: configKey });
  }

  postCheckUserExistUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_CHECK_USER_EXIST);
  }

  postDiscardContentRequestUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_DISCARD_CONTENT_REQUEST);
  }

  postSaveContentDraft() : string {
    return this.getAPIUrl(ApiUrlService.POST_SAVE_CONTENT_DRAFT);
  }

  postBizContentSearch() : string {
    return this.getAPIUrl(ApiUrlService.POST_BIZ_CONTENT_SEARCH);
  }

  postPublishContentRequestUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_PUBLISH_CONTENT_REQUEST);
  }

  postApproveContentRequestUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_APPROVE_CONTENT_REQUEST);
  }

  postRejectContentRequestUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_REJECT_CONTENT_REQUEST);
  }

  postWithdrawContentRequestUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_WITHDRAW_CONTENT_REQUEST);
  }

  postEditContentRequestUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_EDIT_CONTENT_REQUEST);
  }

  postPublishContentRequestListUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_PUBLISH_CONTENT_REQUEST_LIST);
  }

  postApproveContentRequestListUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_APPROVE_CONTENT_REQUEST_LIST);
  }

  postRejectContentRequestListUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_REJECT_CONTENT_REQUEST_LIST);
  }

  postWithdrawContentRequestListUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_WITHDRAW_CONTENT_REQUEST_LIST);
  }

  postDiscardContentRequestListUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_DISCARD_CONTENT_REQUEST_LIST);
  }

  postAddMemberUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_ADD_MEMBER);
  }

  postUpdateMemberUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_UPDATE_MEMBER);
  }

  postDeleteMembersUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_DELETE_MEMBERS);
  }

  postSaveDocstoreConfigUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_SAVE_DOCSTORE_CONFIG);
  }

  postSaveConfigInfoUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_SAVE_CONFIG_INFO);
  }

  postDeleteConfigInfoUrl(configIdentity: string) : string {
    return this.getAPIUrl(ApiUrlService.POST_DELETE_CONFIG_INFO, { configIdentity:  configIdentity });
  }

}

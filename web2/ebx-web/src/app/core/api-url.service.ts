import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable()
export class ApiUrlService {

  static GET_USER = "/user";
  static GET_DEFAULT_CONTENT_TEMPLATE = "/template/default";
  static GET_ALL_RESOURCE_VERSIONS = "/version/all/";
  static GET_CONTENT_RECORD = "/data/t/:templateId/c/:contentQId/d/:recordIdentity";
  static GET_EMBED_INFO = "/urlembed/fetch/";
  static GET_EXT_LINK_URL = "/xlink?u=:url&cId=:cId&cQId=:cQId&atChannel=:atChannel";
  static GET_BOUNDED_REF_LIST = "/bounded/list/:templateId/:contentItemQId/";
  static GET_DOC_PREVIEW_DATA = "/doc/pd/:docIdentity";
  static GET_DOC_DOWNLOAD = "/doc/download/:docIdentity";
  static GET_RECORD_AND_CHILDREN = "/data/rnc/c/:contentQId/r/:contentIdentity";
  static GET_TENANT = "/tenant/fetch";
  static GET_DELETE_CONTENT_RECORD = "/content/delete/t/:templateId/c/:contentQId/r/:recordIdentity";
  static GET_CONTENT_REQUEST = "/contentwf/r/:refObjectIdentity/";
  static GET_ALL_SYSTEM_ROLES = "/roles";
  static GET_MEMBER_PROFILE = "/d/user/:userIdentity/"
  static GET_DEFAULT_DOCSTORE_CONFIG = "/docstore/default";
  static GET_CONFIG_INFO = "/config/:configKey/";
  static GET_RECO_CONTENT = "/navcontent/rlvreco?pageSize=:pageSize&pageNo=:pageNo";

  static GET_CONTENT_WF_STATE_ACTION_MAP = "/contentwf/actionmap/";

  static GET_ALL_MEMBERS = "/systemuser";
  static GET_APP_USER_PREFS = "/userpref/applicable";

  static GET_AUDIT_ACTIVITY_TYPES = "/getAuditConfiguration/getAuditActivityTypes";

  static GET_ACTIVITY_METRIC_TYPES = "/report/getactivitymetrices";
  static GET_ACTIVITY_TREND_DATA = "/activitytrend";
  static GET_ACTIVITY_METRIC_SUMMARY = "/report/activitymetric";

  static POST_FOR_DATA_SEARCH = "/data/search/t/:domainType/";
  static POST_FOR_CONTAINER_DATA_SEARCH = "/data/search/cq/:containerQId/";

  static POST_INSERT_ROOT_CONTAINER_DATA = "/content/update/t/:templateId/c/:contentQId/";
  static POST_UPDATE_CONTAINER_DATA = "/content/update/t/:templateId/c/:contentQId/";
  static POST_INSERT_CHILD_CONTAINER_DATA = "/content/update/t/:templateId/c/:contentQId/r/:parentIdentity";
  static POST_BOUNDED_DEF_REF_LIST = "/bounded/d/list/";
  static POST_FILE_UPLOAD = '/doc/upload';
  static POST_BIZ_CONTENT_SEARCH = '/search/bizcontent/';
  
  static POST_SAVE_CONTENT_DRAFT = '/contentwf/savedraft/';
  static POST_SUBMIT_CONTENT_REQUEST = "/contentwf/submit/";
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
  static POST_DELETE_CONFIG_INFO = "/config/delete/:configIdentity/";

  static POST_SHARE_BY_EMAIL_URL = "/email/sharecontent";
  static POST_FETCH_SHARE_LINK_URL = "/shareOptions/shareableLinkUrl/";
  static POST_FETCH_SHARE_DOC_URL = "/shareOptions/shareableDocUrlv2/";
 
  static POST_RESET_PASSWORD_URL = "/resetpassword";
  static POST_SET_PASSWORD_URL = "/systemuserchangepwd";

  static POST_SAVE_AS_USER_PREF = "/userpref/saveasuserpref";
  static POST_SAVE_AS_SYSTEM_PREF = "/userpref/saveasyspref";

  static GET_LOGOUT = "/logout";

  constructor() { }

  getAPIUrl(url: string, urlPlaceholders?: { [key: string] : string }, prefixBaseAPIUrl: boolean = true) : string {

  	let finalUrl = prefixBaseAPIUrl ? environment.baseAPIUrl + url : url;
  	
  	if (urlPlaceholders != null) {
  	  for (const prop in urlPlaceholders) {
  	  	finalUrl = finalUrl.replace(":" + prop, urlPlaceholders[prop]);
  	  }
  	}

  	//console.log("Endpoint URL: " + finalUrl);
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

  getExtLinkUrl(url: string, cIdentity: string, cQId: string, atChannel: string = 'WEB') : string {
    return this.getAPIUrl(ApiUrlService.GET_EXT_LINK_URL, { url: encodeURIComponent(url), cId: cIdentity, cQId: cQId, atChannel: atChannel});
  }

  getShareExtLinkUrl(url: string, cIdentity: string, cQId: string, atChannel: string = 'WEB') : string {
    return this.getAPIUrl(ApiUrlService.GET_EXT_LINK_URL, { url: encodeURIComponent(url), cId: cIdentity, cQId: cQId, atChannel: atChannel}, false);
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

  getDocDownloadUrl(docIdentity: string) : string {
    return this.getAPIUrl(ApiUrlService.GET_DOC_DOWNLOAD, {docIdentity: docIdentity});
  }

  getShareDocDownloadUrl(docIdentity: string) : string {
    return this.getAPIUrl(ApiUrlService.GET_DOC_DOWNLOAD, {docIdentity: docIdentity}, false);
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

  getContentWFStateActionMapUrl() {
    return this.getAPIUrl(ApiUrlService.GET_CONTENT_WF_STATE_ACTION_MAP);
  }

  getAllMembersUrl() {
    return this.getAPIUrl(ApiUrlService.GET_ALL_MEMBERS);
  }

  getApplicableUserPrefsUrl() {
    return this.getAPIUrl(ApiUrlService.GET_APP_USER_PREFS); 
  }

  getActivityMetricTypesUrl() {
    return this.getAPIUrl(ApiUrlService.GET_ACTIVITY_METRIC_TYPES);   
  }

  getActivityTrendDataUrl() {
    return this.getAPIUrl(ApiUrlService.GET_ACTIVITY_TREND_DATA); 
  }

  getActivityMetricSummaryUrl() {
    return this.getAPIUrl(ApiUrlService.GET_ACTIVITY_METRIC_SUMMARY); 
  }

  getRecoContentUrl(pageSize: number, pageNo: number) : string {
    return this.getAPIUrl(ApiUrlService.GET_RECO_CONTENT, 
          { pageSize: String(pageSize), pageNo: String(pageNo)});
  }

  getAuditActivityTypesUrl() : string {
    return this.getAPIUrl(ApiUrlService.GET_AUDIT_ACTIVITY_TYPES);
  }

  postCheckUserExistUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_CHECK_USER_EXIST);
  }

  postDiscardContentRequestUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_DISCARD_CONTENT_REQUEST);
  }

  postSubmitContentRequestUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_SUBMIT_CONTENT_REQUEST);
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

  postShareByEmailUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_SHARE_BY_EMAIL_URL);
  }

  postResetPasswordUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_RESET_PASSWORD_URL);
  }

  postSetPasswordUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_SET_PASSWORD_URL);
  }

  postSaveAsUserPrefUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_SAVE_AS_USER_PREF); 
  }

  postSaveAsSystemPrefUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_SAVE_AS_SYSTEM_PREF); 
  }

  postFetchShareLinkUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_FETCH_SHARE_LINK_URL);
  }

  postFetchShareDocUrl() : string {
    return this.getAPIUrl(ApiUrlService.POST_FETCH_SHARE_DOC_URL);
  }
}

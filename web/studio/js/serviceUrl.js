enablix.serviceURL = {
	"fetchContentTemplate": enablix.domainName + "/template/:templateId",
	"fetchDefaultContentTemplate": enablix.domainName + "/template/default",
	"fetchRootContainers": enablix.domainName + "/template/containers/:templateId",
	"fetchChildContainers": enablix.domainName + "/template/containers/:templateId/:parentQId/",
	"fetchRootData": enablix.domainName + "/data/t/:templateId/c/:contentQId/?page=:page&size=:pageSize&sortProp=:sortProp&sortDir=:sortDir",
	"fetchChildrenData": enablix.domainName + "/data/t/:templateId/c/:contentQId/p/:parentIdentity?page=:page&size=:pageSize&sortProp=:sortProp&sortDir=:sortDir",
	"fetchRecordAndChildData": enablix.domainName + "/data/rnc/c/:contentQId/r/:contentIdentity?size=:pageSize",
	"fetchRecordData": enablix.domainName + "/data/t/:templateId/c/:contentQId/d/:recordIdentity",
	"fetchContentStack": enablix.domainName + "/data/fetchcs",
	"fetchContentStackForRecord": enablix.domainName + "/data/fetchcs/c/:containerQId/r/:instanceIdentity",
	"insertRootContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/",
	"updateContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/",
	"insertChildContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/r/:parentIdentity",
	"uploadDocument" : enablix.domainName + "/doc/upload",
	"downloadDocument" : enablix.domainName + "/doc/download/",
	"getDocMetadata" : enablix.domainName + "doc/docmd/:docIdentity",
	"fetchBoundedRefList": enablix.domainName + "/bounded/list/:templateId/:contentQId/",
	"fetchBoundedDefRefList": enablix.domainName + "/bounded/d/list/",
	"deleteContentData": enablix.domainName + "/content/delete/t/:templateId/c/:contentQId/r/:recordIdentity",
	"generalRecommendation": enablix.domainName + "/navcontent/reco",
	"containerSpecificRecommendation": enablix.domainName + "/navcontent/reco/:containerQId/",
	"contentSpecificRecommendation": enablix.domainName + "/navcontent/reco/:containerQId/:contentIdentity/",
	"saveRecommendation": enablix.domainName + "/reco/save",
	"deleteRecommendation": enablix.domainName + "/reco/delete",
	"fetchRecommendationList": enablix.domainName + "/reco/list",
	"getRecentDataList": enablix.domainName + "/navcontent/recentlist",
	"generalRecentData": enablix.domainName + "/navcontent/recent/",
	"containerSpecificRecentData": enablix.domainName + "/navcontent/recent/:containerQId/",
	"contentSpecificRecentData": enablix.domainName + "/navcontent/recent/:containerQId/:contentIdentity/",
	"quickLinks": enablix.domainName + "/quicklink/content",
	"addQuickLinks": enablix.domainName + "/quicklink/add",
	"deleteQuickLinks": enablix.domainName + "/quicklink/delete",
	"getQuickLinkCategoryAssociation": enablix.domainName + "/quicklink/categories/:contentIdentity/",
	"saveQuickLinkCategory": enablix.domainName + "/quicklink/category",
	"deleteQuickLinkCategory": enablix.domainName + "/quicklink/category/delete",
	"contentPeers": enablix.domainName + "/navcontent/peers/:containerQId/:contentIdentity/",
	"searchData": enablix.domainName + "/search/t/:searchText/?page=:pageNum&size=:pageSize",
	"navPath": enablix.domainName + "/navcontent/navpath/:containerQId/:contentIdentity/",
	"user": enablix.domainName + "/user",
	"systemuser" : enablix.domainName + "/systemuser",
	"systemuseredit" : enablix.domainName + "/systemuseredit",
	"systemuserchangepwd" : enablix.domainName + "/systemuserchangepwd",
	"fetchUser" : enablix.domainName + "/d/user/:userIdentity/",
	"deletesystemuser" : enablix.domainName + "/deletesystemuser",
	"getemailconfiguration" : enablix.domainName + "/getemailconfiguration",
	"addemailconfiguration" : enablix.domainName + "/addemailconfiguration",
	"deleteemailconfiguration" : enablix.domainName + "/deleteemailconfiguration",
	"getAuditConfiguration" : enablix.domainName + "/data/search/c/ebx_activity_audit/t/com.enablix.core.domain.activity.ActivityAudit/",
	"getAuditActivityTypes" : enablix.domainName + "/getAuditConfiguration/getAuditActivityTypes",
	"getsmtpconfig" : enablix.domainName + "/getsmtpconfig/:domainName",
	"checkusername" : enablix.domainName + "/checkusername",
	"fetchAllRoles" : enablix.domainName + "/roles",
	"fetchConfigByKey" : enablix.domainName + "/config/:configKey",
	"saveConfig" : enablix.domainName + "/config/save",
	"fetchDocStoresConfigMetadata" : enablix.domainName + "/docstore/configmetadata",
	"saveDocStoreConfig" : enablix.domainName + "/docstore/config",
	"defaultDocStoreConfig": enablix.domainName + "/docstore/default",
	"logout": enablix.domainName + "/logout",
	"sendmail": enablix.domainName + "/sendmail",
	"resetpassword": enablix.domainName + "/resetpassword",
	"contactUs": enablix.domainName + "/site/contactus",
	"captchasitekey": enablix.domainName + "/site/captchasitekey",
	"sharecontent": enablix.domainName + "/sharecontent",
	"auditContentAccess": enablix.domainName + "/activity/content/access/",
	"submitContentSuggestion": enablix.domainName + "/contentwf/submit/",
	"approveContentSuggestion": enablix.domainName + "/contentwf/approve/",
	"rejectContentSuggestion": enablix.domainName + "/contentwf/reject/",
	"withdrawContentSuggestion": enablix.domainName + "/contentwf/withdraw/",
	"editContentSuggestion": enablix.domainName + "/contentwf/edit/",
	"getContentSuggestion": enablix.domainName + "/contentwf/r/:refObjectIdentity/",
	"getContentWFStateActionMap": enablix.domainName + "/contentwf/actionmap/",
	"dataSearchRequest": enablix.domainName + "/data/search/t/:domainType/",
	"containerDataSearchRequest": enablix.domainName + "/data/search/cq/:containerQId/",
	"saveContentConnection": enablix.domainName + "/contentconn/save",
	"getContentConnection": enablix.domainName + "/contentconn/r/:connIdentity/",
	"delContentConnection": enablix.domainName + "/contentconn/del/:connIdentity/",
	"getPlayDefinition": enablix.domainName + "/play/def/r/:playDefId",
	"saveOrUpdatePlayDefinition": enablix.domainName + "/play/def/update",
	"updateActiveStatusOfPlayDef": enablix.domainName + "/play/def/active/update",
	"getCorrelatedItemTypeHierarchy": enablix.domainName + "/corr/itemtypes/:sourceItemQId/",
	"getContentSetRecords": enablix.domainName + "/play/data/contentsetrecords/",
	"shareOptsGetPortalURL" : enablix.domainName + "/shareOptions/getPortalURL/",
	"shareOptsEmailClientAudit" : enablix.domainName + "/shareOptions/auditShareViaEmailClient/",
	"shareOptsPortalAudit" : enablix.domainName + "/shareOptions/portalURLAudit/",
	"shareOptsDocDownloadAudit" : enablix.domainName + "/shareOptions/downldDocURLAudit/",
	"loadResourceVersions" : enablix.domainName + "/version/all/",
	"getSlackCode" : "https://slack.com/oauth/authorize?scope=chat:write:user,channels:read&client_id=",
	"getSlackAppDtls" : enablix.domainName + "/slack/getSlackAppDtls/",
	"getSlackAccessToken" : enablix.domainName + "/slack/authorizeSlack/",
	"getSlackStoredAuthAccessToken" : enablix.domainName +"/slack/getStoredSlackToken",
	"unauthSlackAcc" : enablix.domainName +"/slack/unauthSlack",
	"getSlackChannels": enablix.domainName +"/slack/getChannelsLst",
	"sendMessageToSlack": enablix.domainName +"/slack/sendMessage"

};

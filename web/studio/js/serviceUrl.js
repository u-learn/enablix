enablix.serviceURL = {
	"fetchContentTemplate": enablix.domainName + "/template/:templateId",
	"fetchDefaultContentTemplate": enablix.domainName + "/template/default",
	"fetchRootContainers": enablix.domainName + "/template/containers/:templateId",
	"fetchChildContainers": enablix.domainName + "/template/containers/:templateId/:parentQId/",
	"fetchRootData": enablix.domainName + "/data/t/:templateId/c/:contentQId/?page=:page&size=:pageSize",
	"fetchChildrenData": enablix.domainName + "/data/t/:templateId/c/:contentQId/p/:parentIdentity",
	"fetchRecordData": enablix.domainName + "/data/t/:templateId/c/:contentQId/d/:recordIdentity",
	"insertRootContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/",
	"updateContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/",
	"insertChildContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/r/:parentIdentity",
	"uploadDocument" : enablix.domainName + "/doc/upload",
	"downloadDocument" : enablix.domainName + "/doc/download/",
	"fetchBoundedRefList": enablix.domainName + "/bounded/list/:templateId/:contentQId/",
	"deleteContentData": enablix.domainName + "/content/delete/t/:templateId/c/:contentQId/r/:recordIdentity",
	"generalRecommendation": enablix.domainName + "/navcontent/reco",
	"containerSpecificRecommendation": enablix.domainName + "/navcontent/reco/:containerQId/",
	"contentSpecificRecommendation": enablix.domainName + "/navcontent/reco/:containerQId/:contentIdentity/",
	"generalRecentData": enablix.domainName + "/navcontent/recent/",
	"containerSpecificRecentData": enablix.domainName + "/navcontent/recent/:containerQId/",
	"contentSpecificRecentData": enablix.domainName + "/navcontent/recent/:containerQId/:contentIdentity/",
	"quickLinks": enablix.domainName + "/navcontent/quicklinks/",
	"contentPeers": enablix.domainName + "/navcontent/peers/:containerQId/:contentIdentity/",
	"searchData": enablix.domainName + "/search/t/:searchText/",
	"navPath": enablix.domainName + "/navcontent/navpath/:containerQId/:contentIdentity/",
	"user": enablix.domainName + "/user",
	"systemuser" : enablix.domainName + "/systemuser",
	"fetchUser" : enablix.domainName + "/d/user/:userIdentity/",
	"deletesystemuser" : enablix.domainName + "/deletesystemuser",
	"getemailconfiguration" : enablix.domainName + "/getemailconfiguration/:tenantId",
	"addemailconfiguration" : enablix.domainName + "/addemailconfiguration",
	"deleteemailconfiguration" : enablix.domainName + "/deleteemailconfiguration",
	"getsmtpconfig" : enablix.domainName + "/getsmtpconfig/:domainName",
	"checkusername" : enablix.domainName + "/checkusername",
	"fetchAllRoles" : enablix.domainName + "/roles",
	"fetchConfigByKey" : enablix.domainName + "/config/:configKey",
	"saveConfig" : enablix.domainName + "/config/save",
	"fetchDocStoresConfigMetadata" : enablix.domainName + "/docstore/configmetadata",
	"saveDocStoreConfig" : enablix.domainName + "/docstore/config",
	"defaultDocStoreConfig": enablix.domainName + "/docstore/default",
	"logout": enablix.domainName + "/logout",
	"sentmail": enablix.domainName + "/sentmail/:scenario/:userid/:tenantid"
};
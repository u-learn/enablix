enablix.serviceURL = {
	"fetchContentTemplate": enablix.domainName + "/template/:templateId",
	"fetchRootContainers": enablix.domainName + "/template/containers/:templateId",
	"fetchChildContainers": enablix.domainName + "/template/containers/:templateId/:parentQId/",
	"fetchRootData": enablix.domainName + "/data/t/:templateId/c/:contentQId/",
	"fetchChildrenData": enablix.domainName + "/data/t/:templateId/c/:contentQId/p/:parentIdentity",
	"fetchRecordData": enablix.domainName + "/data/t/:templateId/c/:contentQId/d/:recordIdentity",
	"insertRootContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/",
	"updateContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/",
	"insertChildContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/r/:parentIdentity",
	"uploadDocument" : enablix.domainName + "/doc/upload",
	"fetchBoundedRefList": enablix.domainName + "/bounded/list/:templateId/:contentQId/"
};
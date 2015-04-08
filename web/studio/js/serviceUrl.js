enablix.serviceURL = {
	"fetchContentTemplate": enablix.domainName + "/template/:templateId",
	"fetchRootContainers": enablix.domainName + "/template/containers/:templateId",
	"fetchChildContainers": enablix.domainName + "/template/containers/:templateId/:parentQId/",
	"fetchRootData": enablix.domainName + "/data/t/:templateId/c/:contentQId/",
	"fetchChildrenData": enablix.domainName + "/data/t/:templateId/c/:contentQId/p/:parentIdentity",
	"fetchRecordData": enablix.domainName + "/data/t/:templateId/c/:contentQId/d/:recordIdentity",
	"saveOrUpdateRootContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/",
	"saveOrUpdateChildContainerData": enablix.domainName + "/content/update/t/:templateId/c/:contentQId/r/:parentIdentity"
};
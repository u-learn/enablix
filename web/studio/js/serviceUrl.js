enablix.serviceURL = {
	"fetchRootContainers": enablix.domainName + "/template/containers/:templateId",
	"fetchChildContainers": enablix.domainName + "/template/containers/:templateId/:parentQId/",
	"fetchRootData": enablix.domainName + "/data/t/:templateId/c/:contentQId/",
	"fetchChildrenData": enablix.domainName + "/data/t/:templateId/c/:contentQId/p/:parentIdentity",
	"fetchChildRecord": enablix.domainName + "/data/t/:templateId/c/:contentQId/d/:recordIdentity"
};
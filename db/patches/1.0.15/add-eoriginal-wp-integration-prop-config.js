/**************************************************************
Script to create config required by Wordpress Analysers for eOriginal

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

var date = new Date();
var wpConfig = 
	{
		"_class" : "com.enablix.core.domain.config.Configuration",
		"identity" : "wp-integration-properties-config",
		"key" : "wp.integration.properties", 
		"config" : {
			"properties" : {
				"_class" : "com.enablix.wordpress.integration.WPIntegrationProperties",
				"defaultContentTypeQId" : "blog",
				"wpCatToContQId" : {},
				"contQIdToSlugMatchAttrId" : {
					"blog" : "url",
					"press" : "url"
				},
				"linkPatternToContQId" : {
					"**/blog/*" : "blog",
					"**/press-releases/*": "press"
				},
				"tagAliasMapping" : {
					"Audit and Compliance" : ["Compliance", "Auditability"],
					"Auto Financing" : ["Vehicle Finance"],
					"Chattel Paper" : ["eChattel"],
					"Custodian Services" : ["Custodians"],
					"Digital Asset Management" : ["Digital Transaction Management and Digital Asset Management"],
					"Digital Mortgage" : ["Mortgage"],
					"Digital Transactions" : ["Digital Transaction Management", "Digital asset management and digital transactions"],
					"Electronic Chattel Paper" : ["Electronic Chattel"],
					"eMortgage" : ["Mortgage", "Digital Mortgage"],
					"Transaction Management" : ["Digital Transaction Management"]
				}
			}
		}, 
		"createdAt" : date, 
		"modifiedAt" : date, 
		"createdBy" : "system",
		"createdByName" : "System",
		"modifiedBy" : "system",
		"modifiedByName" : "System"
	};

// Switch to haystax database
db = db.getSiblingDB("eoriginal_enablix");
db.createCollection("ebx_configuration");

var coll = db.getCollection("ebx_configuration");
coll.update({"identity": wpConfig.identity}, wpConfig, {upsert: true});

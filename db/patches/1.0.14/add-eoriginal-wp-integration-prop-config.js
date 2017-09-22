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
					"blog" : "url"
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

/**************************************************************
Script to create config required by Wordpress Analyser for Haystax

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

var date = new Date();
var wpConfig = 
	{
		"_class" : "com.enablix.core.domain.config.Configuration",
		"identity" : "wp-post-analyser-type-mapping-config",
		"key" : "wp.post.analyser.type.mapping", 
		"config" : {
			"blog_containerQId" : "blog"
		}, 
		"createdAt" : date, 
		"modifiedAt" : date, 
		"createdBy" : "system",
		"createdByName" : "System",
		"modifiedBy" : "system",
		"modifiedByName" : "System"
	};

// Switch to haystax database
db = db.getSiblingDB("haystax_enablix");
db.createCollection("ebx_configuration");

var coll = db.getCollection("ebx_configuration");
coll.insert(wpConfig);

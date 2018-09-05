/**************************************************************
Script to setup test tenants and users. This is the generic script
which requires <tenant>-config.js to setup tenant variables before 
executing this script. 

Sample <tenant>-config.js

var tenantId = "enablixmaster";
var templateId = "master_enterprise";
var adminUserId = "admin@enablix.com";
var adminUserName = "Enablix Admin";

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<tenant>-config.js <path>/<script-file-name> 

****************************************************************/

var getCollection = function(dbName, collectionName) {
	
	print("Connecting to database: " + dbName);
	
	db = db.getSiblingDB(dbName);
	db.createCollection(collectionName);
	
	return db.getCollection(collectionName);
};

var insertTenant = function() {
	
	print("inserting tenant record...");
	
	var tenant = { 
	    "_id" : tenantId, 
	    "_class" : "com.enablix.core.domain.tenant.Tenant", 
	    "identity" : tenantId, 
	    "tenantId" : tenantId, 
	    "name" : tenantName, 
	    "defaultTemplateId" : templateId
	};
	
	var coll = getCollection("system_enablix", "ebxTenant");
	coll.insert(tenant);	
};

var tenantDBName = function() {
	return tenantId + "_enablix";
}

var insertUsers = function() {
	
	print("inserting admin user...");
	
	var coll = getCollection("system_enablix", "ebxUser");
	
	print("adding " + adminUserId + " user...");
	var date = new Date();
	
	coll.insert(
		{ 
		    "_id" : adminUserId, 
		    "_class" : "com.enablix.core.domain.user.User", 
		    "userId" : adminUserId, 
		    "identity" : adminUserId, 
		    "password" : "bzYm2zY49brHPO9tnK7kSg==", 
		    "tenantId" : tenantId,
		    "isPasswordSet" : false, 
		    "createdAt" : date, 
		    "createdBy" : "system", 
		    "createdByName" : "System", 
		    "modifiedBy" : "system", 
		    "modifiedByName" : "System", 
		    "modifiedAt" : date
		}
	);
	
	// set up roles for tenant database
	coll = getCollection(tenantDBName(), "ebx_role");
	
	var roles = [
		{ 
		    "_id" : "contentAdmin", 
		    "_class" : "com.enablix.core.domain.security.authorization.Role", 
		    "identity" : "contentAdmin", 
		    "roleName" : "Administrator", 
		    "permissions" : [
		    	"VIEW_STUDIO", 
		        "VIEW_REF_DATA", 
		        "VIEW_PORTAL", 
		        "VIEW_RECENT_CONTENT", 
		        "MANAGE_CONTENT_REQUEST", 
		        "SHARE_VIA_EMAIL", 
		        "SHARE_VIA_SLACK", 
		        "MANAGE_INTEGRATIONS", 
		        "VIEW_REPORT-activity-metric-calculator", 
		        "VIEW_REPORT-content-coverage-report", 
		        "VIEW_REPORTS", 
		        "VIEW_REPORT-activity-trend-calculator", 
		        "DOCSTORE_DIRECT_ACCESS",
		        "BULK_IMPORT",
		        "BULK_IMPORT-GOOGLEDRIVE",
		        "BULK_IMPORT-YOUTUBE",
		        "MANAGE_DASHBOARD"
		    ]
		},
		{ 
		    "_id" : "portalUser", 
		    "_class" : "com.enablix.core.domain.security.authorization.Role", 
		    "identity" : "portalUser", 
		    "roleName" : "Consumer", 
		    "permissions" : [
		    	"VIEW_PORTAL", 
		        "VIEW_RECENT_CONTENT", 
		        "SUGGEST_CONTENT", 
		        "SHARE_VIA_EMAIL", 
		        "SHARE_VIA_SLACK"
		    ]
		}];
	
	roles.forEach(function(role) {
		coll.insert(role);
	});
	
	
	// add profile for admin user
	coll = getCollection(tenantDBName(), "ebx_user_profile");
	coll.insert({ 
	    "_class" : "com.enablix.core.domain.user.UserProfile", 
	    "name" : adminUserName, 
	    "email" : adminUserId,
	    "userIdentity" : adminUserId, 
	    "systemProfile" : {
	        "roles" : [
	            DBRef("ebx_role", "contentAdmin"), 
	            DBRef("ebx_role", "portalUser")
	        ], 
	        "sendWeeklyDigest" : false
	    }, 
	    "businessProfile" : {},
	    "identity" : adminUserId + "-pid", 
	    "createdAt" : date, 
	    "createdBy" : "system", 
	    "createdByName" : "System", 
	    "modifiedBy" : "system", 
	    "modifiedByName" : "System", 
	    "modifiedAt" : date
	});
	
};

var loadData = function() {
	insertTenant();
	insertUsers();
};

loadData();
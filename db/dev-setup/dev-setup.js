/**************************************************************
Script to setup test tenants and users
Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

var getCollection = function(dbName, collectionName) {
	
	print("Connecting to database: " + dbName);
	
	db = db.getSiblingDB(dbName);
	db.createCollection(collectionName);
	
	return db.getCollection(collectionName);
};

var insertTenants = function(_tenants) {
	
	print("inserting tenants...");
	
	var coll = getCollection("system_enablix", "ebxTenant");
	
	_tenants.forEach(function(tenant) {

		// inserting test tenant
		print("adding tenant: " + tenant.tenantId);
		coll.insert(tenant);

	});
	
};

var insertUsers = function() {
	
	print("inserting users...");
	
	var coll = getCollection("system_enablix", "ebxUser");
	
	// inserting user1
	print("adding ebxmaster@enablix.com user...");
	
	coll.insert(
		{ 
		    "_id" : "ebxmaster", 
		    "_class" : "com.enablix.core.domain.user.User", 
		    "userId" : "ebxmaster@enablix.com", 
		    "password" : "bzYm2zY49brHPO9tnK7kSg==", 
		    "tenantId" : "enablixmaster",
		    "isPasswordSet" : true, 
		    "createdAt" : ISODate("2016-05-17T07:09:33.195+0000"), 
		    "modifiedAt" : ISODate("2016-05-17T17:43:58.653+0000"), 
		    "createdBy" : "ebxmaster", 
		    "modifiedBy" : "ebxmaster", 
		    "identity" : "ebxmaster"
		}
	);
	
	
	// set up roles for enablixmaster tenant
	coll = getCollection("enablixmaster_enablix", "ebx_role");
	
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
		        "MANAGE_PLAYS", 
		        "VIEW_REPORTS", 
		        "UPLOAD_CONFIG_FILES"
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
		        "SUGGEST_CONTENT"
		    ]
		}];
	
	roles.forEach(function(role) {
		coll.insert(role);
	});
	
	
	// add profile for ebxmaster@enablix.com
	coll = getCollection("enablixmaster_enablix", "ebx_user_profile");
	coll.insert({ 
	    "_id" : "ebxmaster_profile", 
	    "_class" : "com.enablix.core.domain.user.UserProfile", 
	    "name" : "Enablix Master Admin", 
	    "email" : "ebxmaster@enablix.com", 
	    "systemProfile" : {
	        "roles" : [
	            DBRef("ebx_role", "contentAdmin"), 
	            DBRef("ebx_role", "portalUser")
	        ], 
	        "sendWeeklyDigest" : false
	    }, 
	    "businessProfile" : {},
	    "identity" : "78b6fd36-cd6c-435b-ab93-056d1a7fc9dc", 
	    "createdAt" : ISODate("2016-05-17T07:09:33.195+0000"), 
	    "createdBy" : "ebxmaster", 
	    "createdByName" : "ebxmaster", 
	    "modifiedBy" : "ebxmaster", 
	    "modifiedByName" : "ebxmaster", 
	    "modifiedAt" : ISODate("2016-05-17T17:43:58.653+0000"), 
	    "userIdentity" : "ebxmaster"
	});
	
};

var loadData = function() {
	var tenants = [
		{ 
		    "_id" : "test", 
		    "_class" : "com.enablix.core.domain.tenant.Tenant", 
		    "identity" : "test", 
		    "tenantId" : "test", 
		    "name" : "Sample Organization", 
		    "defaultTemplateId" : "enterprise-software-template"
		},
		{ 
		    "_id" : "tenant1", 
		    "_class" : "com.enablix.core.domain.tenant.Tenant", 
		    "identity" : "tenant1", 
		    "tenantId" : "tenant1", 
		    "name" : "Tenant 1 Organization", 
		    "defaultTemplateId" : "amlSalesTemplate"
		},
		{ 
		    "_id" : "simplecrm", 
		    "_class" : "com.enablix.core.domain.tenant.Tenant", 
		    "identity" : "simplecrm", 
		    "tenantId" : "simplecrm", 
		    "name" : "Simple CRM", 
		    "defaultTemplateId" : "simplecrm_enterprise"
		},
		{ 
		    "_id" : "oraclefccm", 
		    "_class" : "com.enablix.core.domain.tenant.Tenant", 
		    "identity" : "oraclefccm", 
		    "tenantId" : "oraclefccm", 
		    "name" : "Oracle FCCM", 
		    "defaultTemplateId" : "oraclefccm_enterprise"
		},
		{ 
		    "_id" : "netbrain", 
		    "_class" : "com.enablix.core.domain.tenant.Tenant", 
		    "identity" : "netbrain", 
		    "tenantId" : "netbrain", 
		    "name" : "NetBrain Central", 
		    "defaultTemplateId" : "netbrain_enterprise"
		},
		{ 
		    "_id" : "lucidchart", 
		    "_class" : "com.enablix.core.domain.tenant.Tenant", 
		    "identity" : "lucidchart", 
		    "tenantId" : "lucidchart", 
		    "name" : "Lucidchart Sales Central", 
		    "defaultTemplateId" : "lucidchart_enterprise"
		},
		{ 
		    "_id" : "enablixmaster", 
		    "_class" : "com.enablix.core.domain.tenant.Tenant", 
		    "identity" : "enablixmaster", 
		    "tenantId" : "enablixmaster", 
		    "name" : "Enablix Master", 
		    "defaultTemplateId" : "master_enterprise"
		},
		{ 
		    "_id" : "haystax", 
		    "_class" : "com.enablix.core.domain.tenant.Tenant", 
		    "identity" : "haystax", 
		    "tenantId" : "haystax", 
		    "name" : "Haystax", 
		    "defaultTemplateId" : "haystax_enterprise"
		},
		{ 
		    "_id" : "eoriginal", 
		    "_class" : "com.enablix.core.domain.tenant.Tenant", 
		    "identity" : "eoriginal", 
		    "tenantId" : "eoriginal", 
		    "name" : "eOriginal Content Center", 
		    "defaultTemplateId" : "eoriginal_enterprise"
		}];
	
	insertTenants(tenants);
	insertUsers();
};

loadData();
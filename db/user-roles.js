/**************************************************************
Script to user roles and assign roles to user
Execute: use mongo utility to run the script

mongo <path>/user-roles.js 

****************************************************************/

var getCollection = function(dbName, collectionName) {
	
	print("Connecting to database: " + dbName);
	
	db = db.getSiblingDB(dbName);
	db.createCollection(collectionName);
	
	return db.getCollection(collectionName);
};

var setupRoles = function(tenantId) {
	
	var coll = getCollection(tenantId + "_enablix", "ebx_role");
	
	// inserting contentAdmin role
	print("inserting role: Content Admin");
	coll.insert(
		{
			"_id" : "contentAdmin",
			"_class" : "com.enablix.core.domain.security.authorization.Role",
			"identity" : "contentAdmin",
			"roleName" : "Content Admin",
			"permissions" : [
				"VIEW_STUDIO",
				"VIEW_REF_DATA"
			]
		}
	);
	
	// inserting contentAdmin role
	print("inserting role: Portal User");
	coll.insert(
		{
			"_id" : "portalUser",
			"_class" : "com.enablix.core.domain.security.authorization.Role",
			"identity" : "portalUser",
			"roleName" : "Portal User",
			"permissions" : [
				"VIEW_PORTAL",
				"VIEW_RECENT_CONTENT"
			]
		}
	);
};

var assignRoleToUser = function(tenantId, userIdentity, rolesJson) {
	
	var coll = getCollection(tenantId + "_enablix", "ebx_user_role");
	
	// inserting contentAdmin role
	print("assigning roles to user [" + userIdentity + "]");
			
	coll.insert(
		{
			"_class" : "com.enablix.core.domain.security.authorization.UserRole",
			"identity" : userIdentity + "_role1",
			"userIdentity" : userIdentity,
			"roles" : rolesJson
		}
	);
};

var loadData = function() {
	setupRoles("test");
	assignRoleToUser("test", "user1", [
				DBRef("ebx_role", "contentAdmin"),
				DBRef("ebx_role", "portalUser") 
			]);
	assignRoleToUser("test", "user3", [
				DBRef("ebx_role", "portalUser") 
			]);
};

loadData();
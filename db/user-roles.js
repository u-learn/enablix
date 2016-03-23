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
	
	// inserting Administrator role
	print("inserting role: Administrator");
	coll.insert(
		{
			"_id" : "Administrator",
			"_class" : "com.enablix.core.domain.security.authorization.Role",
			"identity" : "Administrator",
			"roleName" : "Administrator",
			"permissions" : [
				"VIEW_STUDIO",
				"VIEW_REF_DATA"
			]
		}
	);
	
	// inserting Consumer role
	print("inserting role: Consumer");
	coll.insert(
		{
			"_id" : "Consumer",
			"_class" : "com.enablix.core.domain.security.authorization.Role",
			"identity" : "Consumer",
			"roleName" : "Consumer",
			"permissions" : [
				"VIEW_PORTAL",
				"VIEW_RECENT_CONTENT"
			]
		}
	);
	
	// inserting Contributor role
	print("inserting role: Contributor");
	coll.insert(
		{
			"_id" : "Contributor",
			"_class" : "com.enablix.core.domain.security.authorization.Role",
			"identity" : "Contributor",
			"roleName" : "Contributor",
			"permissions" : [
				"VIEW_PORTAL",
				"VIEW_RECENT_CONTENT",
				"VIEW_STUDIO"
			]
		}
	);
};

var assignRoleToUser = function(tenantId, userIdentity, rolesJson) {
	
	var coll = getCollection(tenantId + "_enablix", "ebx_user_role");
	
	// inserting Administrator role
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
	/*setupRoles("test");
	assignRoleToUser("test", "user1", [
				DBRef("ebx_role", "Administrator"),
				DBRef("ebx_role", "Consumer") 
			]);
	assignRoleToUser("test", "user3", [
				DBRef("ebx_role", "Consumer") 
			]);*/
			
			setupRoles("tenant1");
			assignRoleToUser("tenant1", "user2", [
				DBRef("ebx_role", "Administrator"),
				DBRef("ebx_role", "Consumer") 
			]);
			
			assignRoleToUser("test", "user3", [
				DBRef("ebx_role", "Contributor")
			]);
			
};

loadData();
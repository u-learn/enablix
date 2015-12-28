/**************************************************************
Script to setup test tenants and users
Execute: use mongo utility to run the script

mongo <path>/mongo-setup-data.js 

****************************************************************/

var getCollection = function(dbName, collectionName) {
	
	print("Connecting to database: " + dbName);
	
	db = db.getSiblingDB(dbName);
	db.createCollection(collectionName);
	
	return db.getCollection(collectionName);
};

var insertTenants = function() {
	
	print("inserting tenants...");
	
	var coll = getCollection("system_enablix", "ebxTenant");
	
	// inserting test tenant
	print("inserting tenant: test");
	coll.insert(
		{
			"_id" : "test",
			"_class" : "com.enablix.core.domain.tenant.Tenant",
			"identity" : "test",
			"tenantId" : "test",
			"name" : "Sample Organization",
			"defaultTemplateId" : "enterprise-software-template"
		}
	);
	
	setupRoles("test");
	
	// inserting tenant 1
	print("inserting tenant: tenant1");
	coll.insert(
		{
			"_id" : "tenant1",
			"_class" : "com.enablix.core.domain.tenant.Tenant",
			"identity" : "tenant1",
			"tenantId" : "tenant1",
			"name" : "Tenant 1 Organization",
			"defaultTemplateId" : "amlSalesTemplate"
		}
	);
	
	setupRoles("tenant1");
	
};

var insertUsers = function() {
	
	print("inserting users...");
	
	var coll = getCollection("system_enablix", "ebxUser");
	
	// inserting user1
	print("inserting user: user1");
	coll.insert(
		{
			"_id" : "user1",
			"_class" : "com.enablix.core.domain.user.User",
			"userId" : "user1",
			"identity" : "user1",
			"password" : "password",
			"tenantId" : "test"
		}
	);
	
	// inserting user1
	print("inserting user: user3");
	coll.insert(
		{
			"_id" : "user3",
			"_class" : "com.enablix.core.domain.user.User",
			"userId" : "user3",
			"identity" : "user3",
			"password" : "password",
			"tenantId" : "test"
		}
	);
	
	// inserting tenant 1
	print("inserting user: user2");
	coll.insert(
		{
			"_id" : "user2",
			"_class" : "com.enablix.core.domain.user.User",
			"identity" : "user2",
			"userId" : "user2",
			"password" : "password",
			"tenantId" : "tenant1"
		}
	);
	
};

var loadData = function() {
	insertTenants();
	insertUsers();
};

loadData();
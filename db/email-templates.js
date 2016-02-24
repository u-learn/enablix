/**************************************************************
Script to email templates configuration from backend
Execute: use mongo utility to run the script

mongo <path>/email-templates.js 

****************************************************************/

var getCollection = function(dbName, collectionName) {
	
	print("Connecting to database: " + dbName);
	
	db = db.getSiblingDB(dbName);
	db.createCollection(collectionName);
	
	return db.getCollection(collectionName);
};


var setUpTemplates = function(tenantId) {
	
	var coll = getCollection(tenantId + "_enablix", "ebx_email_template");
coll.insert(
{ 
    "_id" : ObjectId("56c5b983506dd3963c50a5cf"), 
    "identity" : "test", 
    "scenario" : "resetpassword", 
    "templateFile" : "welcome.vm", 
    "name" : "resetPassword"
});
coll.insert(
{ 
    "_id" : ObjectId("56cc1ad743c519ecd0d4357c"), 
    "identity" : "test", 
    "scenario" : "passwordconfirmation", 
    "templateFile" : "passwordConfirmation.vm", 
    "name" : "passwordconfirmation"
}
);

};

var loadData = function() {
	setUpTemplates("test");
};


loadData();
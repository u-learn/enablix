/**************************************************************
Script to set userIdentity and identity attributes of user profile

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/
// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({
    "listDatabases": 1
}).databases;


var getReferenceData = function(userId, tenantDb, templateId) {
    var userTemplateCollection = templateId + "_user";
    userTemplateDoc = tenantDb.getCollection(userTemplateCollection).findOne({
        "email": userId
    }, {
        _id: 0,
        _class: 0,
        createdByName: 0,
        __container: 0,
        modifiedAt: 0,
        userName: 0,
        createdAt: 0,
        modifiedByName: 0,
        createdBy: 0,
        identity: 0,
        __title: 0,
        modifiedBy: 0,
		email: 0,
		"~container~": 0
    });
    return userTemplateDoc;
};

//Iterate through each database and get its collections.
dbs.forEach(function(database) {

    // only run the patch for client databases
    if (database.name.endsWith("_enablix") &&
        database.name != "system_enablix") {

        var tenantId = database.name.substring(0, database.name.indexOf("_enablix"));

        db = db.getSiblingDB(database.name);
        
        // set the user identity = current identity
        db.ebx_user_profile.find({}).snapshot().forEach(
    		function(elem) {
    			print("setting user identity: " + elem._id);
    			db.ebx_user_profile.update(
    					{ 
    						"_id": elem._id
    					},
    					{
    						$set: {
    							"userIdentity": elem.identity
    						}
    					}
    				);
    		});
        
        var templateRecords = db.templateDocument.find({});
        var template = templateRecords[0].template;

        print(" Tenant Id :: " + tenantId + " Database Name :: " + db.name + " Template Name  :: " + template._id);

        // update the identity = identity of reference
        db[template._id + "_user"].find({}).snapshot().forEach(
        	function(elem) {
        		db.ebx_user_profile.update({
        			email: elem.email
        		}, 
        		{
        			$set: {
        				identity: elem.identity
        			}
        		});
        	});
        
        // create user profile for users without login account
        db[template._id + "_user"].find({}).snapshot().forEach(
        	function(elem) {
        		
        		var existUserCnt = db.ebx_user_profile.count({ email: elem.email });
        		
        		print("existing user [" +elem.email + "] count: " + existUserCnt);
        		
        		if (existUserCnt == 0) {
        			
        			var referenceData = getReferenceData(elem.email, db, template._id);
        			
        			db.ebx_user_profile.insert({
        				"_class": "com.enablix.core.domain.user.UserProfile",
                        "name": elem.userName,
                        "email": elem.email,
                        "systemProfile": {
                            "roles": [],
                            "sendWeeklyDigest": false
                        },
                        "businessProfile": {
                            "attributes": referenceData
                        },
                        "identity": elem.identity,
                        "createdAt": elem.createdAt,
                        "createdBy": elem.createdBy,
                        "createdByName": elem.createdBy,
                        "modifiedBy": elem.modifiedBy,
                        "modifiedByName": elem.modifiedBy,
                        "modifiedAt": elem.modifiedAt
        			});
        		}
        	});
        
    }
});
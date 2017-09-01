/**************************************************************
Update the actor field of activity audit as registered user where the 
user exists in system but is recorded as non-registered user when the 
user is not logged-in to the system.

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({
    "listDatabases": 1
}).databases;


//Iterate through each database and get its collections.
dbs.forEach(function(database) {

    // only run the patch for client databases
    if (database.name.endsWith("_enablix") &&
        database.name != "system_enablix") {
        
    	var tenantId = database.name.substring(0, database.name.indexOf("_enablix"));

		// get all tenant users from system database
		systemDb = db.getSiblingDB("system_enablix");
		var tenantUsers = systemDb.ebxUser.find({
		    "tenantId": tenantId
		});
    	
    	db = db.getSiblingDB(database.name);
    	
    	tenantUsers.forEach(function(user) {
    		
    		var userProfile = db.ebx_user_profile.find({ "userIdentity": user.identity });
    		
    		db.ebx_activity_audit.update(
    				{ "actor._class" : "com.enablix.core.domain.activity.NonRegisteredActor", "actor.externalId": user.userId },
    				{ "$set": { 
    						"actor": {
    							"_class" : "com.enablix.core.domain.activity.RegisteredActor", 
    					        "name" : userProfile.name, 
    					        "userId" : user.userId
    						}
    					}
    				},
    				{ multi: true}
    		);
    	});
        
    }
    
});

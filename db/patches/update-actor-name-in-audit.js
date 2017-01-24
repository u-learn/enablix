/**************************************************************
Script to set actor name in audit records

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({ "listDatabases": 1 }).databases;


//Iterate through each database and get its collections.
dbs.forEach(function(database) {
		
	// only run the patch for client databases
	if (database.name.endsWith("_enablix") 
			&& database.name != "system_enablix") {
		
		var tenantId = database.name.substring(0, database.name.indexOf("_enablix"));
		
		// get all tenant users from system database
		systemDb = db.getSiblingDB("system_enablix");
		var tenantUsers = systemDb.ebxUser.find({"tenantId" : tenantId});
		
	    db = db.getSiblingDB(database.name);
	
	    tenantUsers.forEach(function(usr) {
	    	
	    	// update all audit records which do not have the actor name for this record
			db.ebx_activity_audit.update(
				{ 
					"actor.userId": usr.userId,
					"actor.name": { $not: { $exists: true } }
				},
				{
					$set: {
						"actor.name": usr.profile.name
					}
				},
				{
					multi: true
				}
			);
	    });
	    
	}
});
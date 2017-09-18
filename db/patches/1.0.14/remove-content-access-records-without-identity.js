/**************************************************************
Update the activity audit records where the content identity is undefined
and activity type is CONTENT_ACCESS

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
        
    	db = db.getSiblingDB(database.name);
    	
    	db.ebx_activity_audit.remove({"activity.activityType": "CONTENT_ACCESS", "activity.itemIdentity": "undefined"});
        
    }
    
});

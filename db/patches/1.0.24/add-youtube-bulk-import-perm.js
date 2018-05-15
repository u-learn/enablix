/**************************************************************
Script to add permissions for youtube import wizard 
to admin user roles for all tenants.

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
    if (database.name.endsWith("_enablix")) {
        
    	db = db.getSiblingDB(database.name);
    	
    	var coll = database.name == "system_enablix" ? "ref_ebx_role" : "ebx_role";
    	
        db[coll].update(
    		{ _id: 'contentAdmin' },
    		{ $addToSet: { permissions: { $each: ["BULK_IMPORT-YOUTUBE"] } } }
        );
    }
});

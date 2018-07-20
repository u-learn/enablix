/**************************************************************
Script to add "Approved Contributor" role to all tenants

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
    		{ _id: 'contentCreator' },
    		{ 
    		    "_id" : "contentCreator", 
    		    "_class" : "com.enablix.core.domain.security.authorization.Role", 
    		    "identity" : "contentCreator", 
    		    "roleName" : "Approved Contributor", 
    		    "permissions" : [
    		        "VIEW_STUDIO", 
    		        "VIEW_REF_DATA", 
    		        "VIEW_PORTAL", 
    		        "VIEW_RECENT_CONTENT"
    		    ]
    		},
    		{
    			upsert: true
    		}
        );
    }
});

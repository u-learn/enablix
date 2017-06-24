/**************************************************************
Standardize the field names for Activity audit across the activity categories

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
        
        db.ebx_activity_audit.updateMany(
    		{ },
    		{ $rename: {"activity.accountActivityType" : "activity.activityType"} }
        );
        
        db.ebx_activity_audit.update(
        	{ "activity.category" : "NAVIGATION" },
        	{ $set: { "activity.activityType" : "NAV_EXTERNAL_LINK" }},
        	{ multi: true}
        );
        
        db.ebx_activity_audit.update(
        	{ "activity._class" : "com.enablix.core.domain.activity.SearchActivity" },
        	{ $set: { "activity.activityType" : "SEARCH_FREE_TEXT" }},
        	{ multi: true}
        );
        
        db.ebx_activity_audit.update(
        	{ "activity._class" : "com.enablix.core.domain.activity.SuggestedSearchActivity" },
        	{ $set: { "activity.activityType" : "SUGGESTED_SEARCH" }},
        	{ multi: true}
        );
    }
});

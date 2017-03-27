/**************************************************************
Script to set userProfileIdentity in item-user correlation as the domain object
has changed after user-reconciliation activity

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
        
        // update the userProfileIdentity = user.instanceIdentity
        db.ebx_item_user_correlation.find({}).snapshot().forEach(
        	function(elem) {
        		db.ebx_item_user_correlation.update({
        			_id: elem._id
        		}, 
        		{
        			$set: {
        				userProfileIdentity: elem.user.instanceIdentity
        			},
        			$unset: {
        				user: ""
        			}
        		});
        	});
        
    }
});
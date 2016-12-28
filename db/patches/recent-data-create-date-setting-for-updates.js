/**************************************************************
Script to set the "createdAt" of the "UPDATED" type records to be 
same as the "modifiedAt" value as we will run sorting on the "createdAt"
field instead of "modifiedAt"

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
		
	    db = db.getSiblingDB(database.name);
	
	    // now update the record which have an UPDATED record. 
	    // set the created at date to be same as modified at date
	    db.ebx_recent_update.find({ "updateType": "UPDATED" }).snapshot().forEach(
    		function(elem) {
    			db.ebx_recent_update.update(
					{ 
						_id: elem._id
					},
					{
						$set: {
							createdAt: elem.modifiedAt,
							createdBy: elem.modifiedBy
						}
					},
					{
						multi: true
					}
				);
    		}
    	);
	}
});
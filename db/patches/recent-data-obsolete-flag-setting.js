/**************************************************************
Script to set the "obsolete" flag in recent data collection

Execute: use mongo utility to run the script

mongo <path>/<script-file-name>

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
	
	    // first set all to false
	    db.ebx_recent_update.update({},
			{
				$set: {
					"obsolete": false
				},
				
			},
			{
				multi: true
			}
		);
	    
	    // now update the record which have an UPDATED record. 
	    // set the value to true for them
	    db.ebx_recent_update.find({ "updateType": "UPDATED" }).snapshot().forEach(
    		function(elem) {
    			db.ebx_recent_update.update(
					{ 
						"updateType": "NEW",
						"data.instanceIdentity": elem.data.instanceIdentity
					},
					{
						$set: {
							"obsolete": true
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
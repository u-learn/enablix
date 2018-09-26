/**************************************************************
Script to update archived flag in all collections records
where the value is missing.

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
  	
  	// find the template
    var templateRecords = db.templateDocument.find({});
    
    var templateRecord = templateRecords[0];
    
    if (templateRecord) {
  	
    	var templateId = templateRecord.template._id;
    	
    	db.getCollectionNames().forEach(function(c) {
    		
    		if (c.startsWith(templateId)) {
    			
	    		db.getCollection(c).update(
					{ "archived": { $exists: false } },
					{ $set: { "archived" : false }},
					{ multi: true }
				);
    		}
    	});
    }
    
  }
});

/**************************************************************
Script to update requestType for ebx_content_approval records
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
  	
	db.ebx_content_approval.update(
		{ "objectRef.requestType": null, "objectRef.addRequest": true },
		{ $set: { "objectRef.requestType" : "ADD" } },
		{ multi: true }
	);
	
	db.ebx_content_approval.update(
		{ "objectRef.requestType": null, "objectRef.addRequest": false },
		{ $set: { "objectRef.requestType" : "UPDATE" } },
		{ multi: true }
	);
  		
  }
});

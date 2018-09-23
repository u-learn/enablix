/**************************************************************
Script to update contentQId for ebx_content_approval records
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
  	
  	db.ebx_content_approval.find({ "objectRef.data.file.previewStatus": "PENDING"})
  			.snapshot().forEach(function(rec) {
  		
  		var contentQId = rec.objectRef.data.file.contentQId;
  		var docIdentity = rec.objectRef.data.file.identity;
  		
  		if (!contentQId) {
  			contentQId = rec.objectRef.contentQId + ".file";
  			db.ebx_content_approval.update(
  				{ _id: rec._id },
  				{ $set: { "objectRef.data.file.contentQId" : contentQId } }
  			);
  		}
  		
  		if (contentQId) {
  			db.disk_document_metadata_ebx.update(
  				{ "identity": docIdentity, "contentQId": null },
  				{ $set: { "contentQId": contentQId } }
  			);
  		}
  		
  	});
  }
});

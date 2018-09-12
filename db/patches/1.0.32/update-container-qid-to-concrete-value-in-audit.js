/**************************************************************
Script to update the linked container qId to concrete container qId

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({ "listDatabases": 1 }).databases;


//Iterate through each database and get its collections.
dbs.forEach(function(database) {
	
	// helper method
	
	var isLinkedContainer = function(cntnrDef) {
		
		
		if (cntnrDef && cntnrDef.linkContainerQId && cntnrDef.linkContainerQId.length > 0) {
			print("Returning true");
			return true;
		}
		
		print("Returning false");
		return false;
	};
	
	var updateAuditContainerQId = function(dbRef, linkedCntnrQId, concreteCntnrQId) {
		
		dbRef.ebx_activity_audit.update(
			{ "activity.containerQId" : linkedCntnrQId },
			{ $set : { "activity.containerQId" : concreteCntnrQId } },
			{ multi: true }
		);
		
	}
	
	var processContainer = function(dbRef, cntnrDef) {
		
		if (cntnrDef.container) {
			
			cntnrDef.container.forEach(function(childCntnr) {
				
				print("Checking linked container for - " + childCntnr.qualifiedId);
				
				if (isLinkedContainer(childCntnr)) {
					
					print("updating [" + childCntnr.qualifiedId + "] to [" + childCntnr.linkContainerQId + "]");
					updateAuditContainerQId(dbRef, childCntnr.qualifiedId, childCntnr.linkContainerQId);
				} else {
					processContainer(dbRef, childCntnr);
				}
			});
		}
	};
	
	// only run the patch for client databases
	if (database.name.endsWith("_enablix") 
			&& database.name != "system_enablix") {
		
	    db = db.getSiblingDB(database.name);
	
	    // find the template
	    var templateRecords = db.templateDocument.find({});
	    
	    var templateRecord = templateRecords[0];
	    
	    if (templateRecord) {
		    
	    	var template = templateRecord.template;
		    
		    print("processing in database [" + db.name + "] for template [" + template._id + "]");
		    
		    // traverse through all the containers in the template
		    template.dataDefinition.container.forEach(function(cntnr) {		    	
		    	processContainer(db, cntnr)
		    });
	    }
	    
	}
});
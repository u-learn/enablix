/**************************************************************
Script to remove identity field from bounded item values

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> --eval "var tenantId = '<tenantId>', containerQId = '<containerQId>';" <path>/<script-file-name> 

****************************************************************/

// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({ "listDatabases": 1 }).databases;

//Iterate through each database and get its collections.
dbs.forEach(function(database) {
	
	// helper method

	var getCollectionName = function(template, cntnrDef) {
		return template._id + "_" + cntnrDef._id;
	}
	
	var processContainer = function(dbRef, cntnrDef, collName) {
		
		if (cntnrDef.contentItem) {
			
			var boundedItemIds = [];
			cntnrDef.contentItem.forEach(function(ci) {
				
				if (ci.type == 'BOUNDED') {
					boundedItemIds.push(ci._id);
				} 
			});
			
			if (boundedItemIds.length > 0) {
				print("Bounded items: " + boundedItemIds);
				
				var unsetClause = {};
				
				boundedItemIds.forEach(function(bi) {
					unsetClause[bi + ".0.identity"] = 1;
					unsetClause[bi + ".1.identity"] = 1;
					unsetClause[bi + ".2.identity"] = 1;
					unsetClause[bi + ".3.identity"] = 1;
					unsetClause[bi + ".4.identity"] = 1;
				});
				
				printjson(unsetClause);
				
				print("updating collection: " + collName);
				
				var result = dbRef[collName].update(
								{},
								{ $unset: unsetClause },
								{ multi: true }
							);
				
				printjson(result);
			}
		}
	};
	
	// only run the patch for client databases
	if (database.name == (tenantId + "_enablix")) {
		
	    db = db.getSiblingDB(database.name);
	
	    // find the template
	    var templateRecords = db.templateDocument.find({});
	    
	    var templateRecord = templateRecords[0];
	    
	    if (templateRecord) {
		    
	    	var template = templateRecord.template;
		    
		    print("processing in database [" + db.name + "] for template [" + template._id + "]");
		    
		    // traverse through all the containers in the template
		    template.dataDefinition.container.forEach(function(cntnr) {
		    	if (cntnr.qualifiedId == containerQId) {
		    		processContainer(db, cntnr, getCollectionName(template, cntnr));
		    	}
		    });
	    }
	    
	}
});
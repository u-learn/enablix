/**************************************************************
Script to set the item title in audit records where it is missing.
Also set a "__title" attribute in each record.
This is same as an earlier patch but this one goes through nested
containers as well.

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({ "listDatabases": 1 }).databases;


//Iterate through each database and get its collections.
dbs.forEach(function(database) {
	
	// helper method
	var getUIDefinition = function(_template, _elementQId) {
		
		var elemUIDef = undefined;
		
		var uiDefinitions = _template.uiDefinition.contentUIDef;
		
		// find the container UI definition
		for (var i = 0; i < uiDefinitions.length; i++) {

			var uiDef = uiDefinitions[i]; 
			
			if (uiDef.qualifiedId == _elementQId) {
				elemUIDef = uiDef;
				break;
			}
		}
		
		return elemUIDef;
	};

	var getContainerLabelAttrId = function(_template, _containerQId) {
		
		var cntnrUIDef = getUIDefinition(_template, _containerQId);
		
		var labelFieldId = null;
		
		if (cntnrUIDef != undefined || cntnrUIDef != null) {
			
			var labelQId = cntnrUIDef.container.labelQualifiedId;
			
			if (labelQId == undefined || labelQId == null) {
				// check for portal heading config
				if (cntnrUIDef.container.portalConfig && cntnrUIDef.container.portalConfig.headingContentItem) {
					labelQId = cntnrUIDef.container.portalConfig.headingContentItem._id;
				}
			}

			if (labelQId != undefined && labelQId != null) {
			
				if (labelQId.indexOf(_containerQId) == 0) { // starts with container QId, strip it
					labelFieldId = labelQId.substring(_containerQId.length + 1, labelQId.length);
					
				} else {
					labelFieldId = labelQId;
				}
			} 
		}
		
		return labelFieldId;
	};
	
	// only run the patch for client databases
	if (database.name.endsWith("_enablix") 
			&& database.name != "system_enablix") {
		
	    db = db.getSiblingDB(database.name);
	
	    // find the template
	    var templateRecords = db.templateDocument.find({});
	    
	    var template = templateRecords[0].template;
	    
	    print("processing in database [" + db.name + "] for template [" + template._id + "]");
	    
	    var updateLabelForContainers = function(_cntnrList) {
	    	
	    	if (_cntnrList != null && _cntnrList != undefined) {
	    		
	    		// traverse through all the containers in the template
	    	    _cntnrList.forEach(function(cntnr) {
	    	    	
	    	    	if (!cntnr.refData) {
	    	    		
	    	    		print("updating for container: " + cntnr._id)
	    	    		
	    	    		// resolve the collection name
	    	    		var collName = template._id + "_" + cntnr._id;
	    	    		var labelAttrId = getContainerLabelAttrId(template, cntnr.qualifiedId);
	    	    		
	    	    		print("title attribute:" + labelAttrId);
	    	    		
	    	    		if (labelAttrId != null && labelAttrId != undefined) {
		    	    		
	    	    			db[collName].find({}).snapshot().forEach(function(record) {
		    	    			
		    	    			var title = record[labelAttrId];
		    	    			
		    	    			if (title != undefined && title != null) {
		    	    				
		    	    				// update all audit records which do not have the title for this record
		    	    				db.ebx_activity_audit.update(
		        						{ 
		        							"activity.category": "CONTENT",
		        							"activity.itemIdentity": record.identity,
		        							"activity.itemTitle": { $not: { $exists: true } }
		        						},
		        						{
		        							$set: {
		        								"activity.itemTitle": title
		        							}
		        						},
		        						{
		        							multi: true
		        						}
		        					);
		    	    				
		    	    				// set the title in record as well
		    	    				record.__title = title;
		    	    				db[collName].save(record);
		    	    				
		    	    			}
		    	    		});
		    	    		
	    	    		}
	    	    		
	    	    		updateLabelForContainers(cntnr.container);
	    	    	}
	    	    	
	    	    });
	    	}
	    }
	    
	    // traverse through all the containers in the template
	    updateLabelForContainers(template.dataDefinition.container);
	    
	}
});
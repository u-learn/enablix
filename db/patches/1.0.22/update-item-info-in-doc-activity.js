/**************************************************************
Update item fields in document activities

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({
    "listDatabases": 1
}).databases;


function getParentQId(_qId) {
	var parentQId = _qId;
	var lastIndx = _qId.lastIndexOf(".");
	if (lastIndx > 0) {
		parentQId = _qId.substr(0, lastIndx);
	}
	return parentQId;
}

function getChildId(_qId) {
	var childId = _qId;
	var lastIndx = _qId.lastIndexOf(".");
	if (lastIndx > 0) {
		childId = _qId.substr(lastIndx + 1, _qId.length);
	}
	return childId;
}

function getItemTitle(docIdentity, docQId, templateId, _db) {
	
	var cntnrQId = getParentQId(docQId);
	var docItemId = getChildId(docQId);
	var cntnrId = getChildId(cntnrQId);
	
	var collName = templateId + "_" + cntnrId;
	
	var query = {};
	query[docItemId + ".identity"] = docIdentity;
	
	var cursor = _db[collName].find(query);
	var record = cursor.hasNext() ? cursor.next() : null;
	
	return record ? { identity : record.identity, title: record.__title } : null;
}

//Iterate through each database and get its collections.
dbs.forEach(function(database) {

    // only run the patch for client databases
    if (database.name.endsWith("_enablix") &&
        database.name != "system_enablix") {
        
    	db = db.getSiblingDB(database.name);
    	
    	print("processing in database [" + db.name + "]");
    	
    	// find the template
	    var templateRecords = db.templateDocument.find({});
	    
	    if (!templateRecords || templateRecords.length == 0) {
	    	return;
	    }
	    
	    var templateDoc = templateRecords.hasNext() ? templateRecords.next() : null;
	    
	    if (!templateDoc) {
	    	print("Template not found for database [" + db.name + "]");
	    	return;
	    }
	    
	    var templateId = templateDoc.template._id;
	    
    	db = db.getSiblingDB(database.name);
    	
    	db.ebx_activity_audit.find({ "activity.activityType": { $in: ["DOC_DOWNLOAD", "DOC_UPLOAD"] }, "activity.docQId": { $exists: false } })
    		.snapshot().forEach(
    				
	    		function(activityAudit) {
	    			
	    			//print(tojson(activityAudit));
	    			
	    			var docQId = activityAudit.activity.containerQId;
	    			var docIdentity = activityAudit.activity.docIdentity;
	    			
	    			print("processing: " + docQId + ", " + docIdentity);
	    			
	    			if (!docQId) {
	    				print("docQId is null")
	    				print(tojson(activityAudit));
	    				return;
	    			}
	    			
	    			var containerQId = getParentQId(docQId);
	    			
	    			if (docQId !== containerQId) {
	    				
	    				var docTitle = activityAudit.activity.itemTitle;
	    				var item = getItemTitle(docIdentity, docQId, templateId, db);
	    				
	    				var itemTitle = docTitle;
	    				var itemIdentity = null;
	    				
	    				if (item) {
	    					itemIdentity = item.identity;
	    					itemTitle = item.title;
	    				}
	    				
	    				print(docQId + ", " + containerQId + ", " + docTitle + ", " + itemIdentity + ", " + itemTitle);
	    				
	    				db.ebx_activity_audit.update(
        					{ "_id": activityAudit._id },
        					{
        						$set: {
        							"activity.docQId" : docQId,
        							"activity.containerQId" : containerQId,
        							"activity.docTitle" : docTitle,
        							"activity.itemTitle" : itemTitle,
        							"activity.itemIdentity" : itemIdentity
        						}
        					}
    	    			);
	    			}
	    			
	    		}
    		);
    	        
    }
    
});

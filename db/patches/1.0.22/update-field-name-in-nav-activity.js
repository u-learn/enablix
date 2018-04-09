/**************************************************************
Update fields name in navigation activities

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

function getItemTitle(recIdentity, cntnrQId, templateId, _db) {
	
	var cntnrId = getChildId(cntnrQId);
	
	var collName = templateId + "_" + cntnrId;
	
	var cursor = _db[collName].find({identity: recIdentity});
	var record = cursor.hasNext() ? cursor.next() : null;
	
	return record ? record.__title : null;
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
    	
    	db.ebx_activity_audit.find({ "activity.activityType": { $in: ["NAV_EXTERNAL_LINK"] }, "activity.itemTitle": null })
    		.snapshot().forEach(
    				
	    		function(activityAudit) {
	    			
	    			var contentQId = activityAudit.activity.contentItemQId || activityAudit.activity.containerQId;
	    			contentQId = getParentQId(contentQId);
	    			
	    			var recIdentity = activityAudit.activity.contentIdentity || activityAudit.activity.itemIdentity;
	    			
	    			print("processing: " + contentQId + ", " + recIdentity);
	    			
	    			if (!contentQId) {
	    				print("contentQId is null")
	    				print(tojson(activityAudit));
	    				return;
	    			}
	    			
    				var itemTitle = getItemTitle(recIdentity, contentQId, templateId, db);
    				
    				print(contentQId + ", " + recIdentity + ", " + itemTitle);
    				
    				db.ebx_activity_audit.update(
    					{ "_id": activityAudit._id },
    					{
    						$set: {
    							"activity.containerQId" : contentQId,
    							"activity.itemTitle" : itemTitle,
    							"activity.itemIdentity" : recIdentity
    						},
    						$unset: {
    							"activity.contentItemQId": "",
    							"activity.contentIdentity": ""
    						}
    					}
	    			);
	    			
	    		}
    		);
    	        
    }
    
});

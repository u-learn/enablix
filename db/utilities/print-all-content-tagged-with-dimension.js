/**************************************************************
Script to print content items tagged with a dimension type

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> --eval "var tenantId='<tenantId>', dimQId='<dimContainerQId>';" <path>/<script-file-name>

e.g.
mongo localhost:27017/admin -u enablix_app -p <password> --eval "var tenantId='forrita', dimQId='product';" <path>/<script-file-name>

****************************************************************/

var getContainerByQId = function(template, containerQId) {
	
	for (var i = 0; i < template.dataDefinition.container.length; i++) {
		var cntnrDef = template.dataDefinition.container[i];
		if (cntnrDef.qualifiedId == containerQId) {
			return cntnrDef;
		}
	}
	
	return null;
}

var findContentItemByDatastore = function(containerDef, datastoreId) {
	
	if (containerDef) {
	
		for (var k = 0; k < containerDef.contentItem.length; k++) {
		
			var ci = containerDef.contentItem[k];
			if (ci.bounded && ci.bounded.refList && ci.bounded.refList.datastore
					&& ci.bounded.refList.datastore.storeId == datastoreId) {
				return ci;
			}
		}
	}
	
	return null;
}

var findContentStackItem = function(containerDef) {
	
	if (containerDef) {
		
		for (var k = 0; k < containerDef.contentItem.length; k++) {
		
			var ci = containerDef.contentItem[k];
			if (ci.type == 'CONTENT_STACK') {
				return ci;
			}
		}
	}
	
	return null;
}

var getCollectionName = function(template, containerDef) {
	return template._id + "_" + containerDef._id;
}

var addDimValueIfNotPresent = function(existValues, newVal) {
	var indx = getDimValueIndex(existValues, newVal);
	if (indx == -1) {
		existValues.push(newVal);
	}
}

var getDimValueIndex = function(dimValCollection, matchDimVal) {
	var indx = -1;
	for (var m = 0; m < dimValCollection.length; m++) {
		var existVal = dimValCollection[m];
		if (existVal.id == matchDimVal.id) {
			indx = m;
			break;
		}
	}
	return indx;
}

var containsDimValue = function(lookInColl, forVal) {
	return getDimValueIndex(lookInColl, forVal) != -1;
}

var setDimFlagOnFlatRecord = function(allDimValues, recDimValues, flatRec, keys) {
	
	for (var t = 0; t < allDimValues.length; t++) {
		
		var dimVal = allDimValues[t];
		
		var key = dimVal.label;
		if (keys && keys.indexOf(key) < 0) {
			keys.push(key);
		}
		
		flatRec[key] = containsDimValue(recDimValues, dimVal) ? 'x' : '';
	}
}

var flatRecordsToCsv = function(flatRecords, recKeys) {
	
	var fields = recKeys
	
	var replacer = function(key, value) { return value === null ? '' : value }
	
	var csv = flatRecords.map(function(row){
		return fields.map(function(fieldName){
			return JSON.stringify(row[fieldName], replacer)
		}).join(',')
	});
	
	csv.unshift(fields.join(',')) // add header column

	print(csv.join('\r\n'))
}

// Switch to tenant db
db = db.getSiblingDB(tenantId + "_enablix");
      
  	
// find the template
var templateRecords = db.templateDocument.find({});

var templateRecord = templateRecords[0];

if (templateRecord) {
	
	var template = templateRecord.template;
	
	var dimContainer = getContainerByQId(template, dimQId);
	if (dimContainer) {
		
		var printRecords = [];
		
		var printKeys = ['contentIdentity', 'contentTitle', 'contentType', dimContainer.label];
		
		var allDimensionValues = [];
		
		// find all records of a container
		var dimCollName = getCollectionName(template, dimContainer);
		db.getCollection(dimCollName).find({}).snapshot().forEach(function(dimVal) {
			allDimensionValues.push({ id: dimVal.identity, label: dimVal.__title });
			printKeys.push(dimVal.__title);
		});
		
		template.dataDefinition.container.forEach(function(cntnr) {
			
			var tcDimContentItem = findContentItemByDatastore(cntnr, dimQId);
			
			if (tcDimContentItem) {
				
				var contentCollName = getCollectionName(template, cntnr); 
				
				db.getCollection(contentCollName).find({}).snapshot().forEach(function(contentRec) {
					
					var printRec = {
							contentIdentity: contentRec.identity,
							contentTitle: contentRec.__title,
							contentType: cntnr.label
						}
					
					var recDimValues = contentRec[tcDimContentItem._id] || [];
					
					setDimFlagOnFlatRecord(allDimensionValues, recDimValues, printRec);
					
					printRecords.push(printRec);
				});
			}
			
		});
		
		flatRecordsToCsv(printRecords, printKeys);
		
	}
	
	
} else {
	print("Content Template not found for tenant [" + tenantId + "]");
}
    

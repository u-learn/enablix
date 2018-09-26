/**************************************************************
Script to print dimensions tagged in a content kit and its content stack

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> --eval "var tenantId='<tenantId>', targetContentQId='<containerQId>', dimQId='<dimContainerQId>';" <path>/<script-file-name>

e.g.
mongo localhost:27017/admin -u enablix_app -p <password> --eval "var tenantId='forrita', targetContentQId='salesKit', dimQId='product';" <path>/<script-file-name>

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
	var targetCntnrDef = getContainerByQId(template, targetContentQId);
	
	if (targetCntnrDef) {
		
		var tcDimContentItem = findContentItemByDatastore(targetCntnrDef, dimQId);
		
		if (tcDimContentItem) {
			
			var tcContentStackItem = findContentStackItem(targetCntnrDef);
			
			if (tcContentStackItem) {
				
				var anchorCollName = getCollectionName(template, targetCntnrDef);
				
				var anomalies = [];
				
				db.getCollection(anchorCollName).find({}).forEach(function(rec) {
					
					var recAnomalies = {
							record: {
								identity: rec.identity,
								title: rec.__title,
								containerLabel: targetCntnrDef.label,
								dimLabel: tcDimContentItem.label,
								allDimValues: []
							},
							stackItems: [] 
						};
					
					anomalies.push(recAnomalies);
					
					// find the reference dim values for comparison
					var refDimValues = rec[tcDimContentItem._id];
					recAnomalies.record.dimValues = refDimValues || [];
					
					// add to all dim values
					recAnomalies.record.dimValues.forEach(function(dv) { recAnomalies.record.allDimValues.push(dv); });
					
					// if (refDimValues && refDimValues.length > 0) {
						
						/*var dimValueIds = [];
						refDimValues.forEach(function(dimVal) {
							dimValueIds.push(dimVal.id);
						});*/
						
						// find the records in content stack
						var stackItems = rec[tcContentStackItem._id];
						if (stackItems && stackItems.length > 0) {
							
							// group stack item ids by qualified id
							var stackItemIdGroup = {};
							
							stackItems.forEach(function(stackItem) {
								var itemIdGrp = stackItemIdGroup[stackItem.qualifiedId];
								if (!itemIdGrp) {
									itemIdGrp = stackItemIdGroup[stackItem.qualifiedId] = [];
								}
								itemIdGrp.push(stackItem.identity);
							});
							
							// for each type of item, find the records which have discripancy in tagging
							for (var qId in stackItemIdGroup) {
								
								var itemCntnrDef = getContainerByQId(template, qId);
								
								if (itemCntnrDef) {
									
									var icDimContentItem = findContentItemByDatastore(itemCntnrDef, dimQId);
									
									if (icDimContentItem) {
										
										var query = { "identity": { $in: stackItemIdGroup[qId] } };
										//query[icDimContentItem._id + ".id"] = { $nin: dimValueIds };
										
										var itemCollName = getCollectionName(template, itemCntnrDef);
										
										db.getCollection(itemCollName).find(query).forEach(function(errRec) {
											
											var itemDimValues = errRec[icDimContentItem._id] || [];
											
											recAnomalies.stackItems.push({
												identity: errRec.identity,
												title: errRec.__title,
												qualifiedId: itemCntnrDef.qualifiedId,
												containerLabel: itemCntnrDef.label,
												dimLabel: icDimContentItem.label,
												dimValues: itemDimValues
											});
											
											itemDimValues.forEach(function(idv) { 
												addDimValueIfNotPresent(recAnomalies.record.allDimValues, idv); 
											});
										});
									}
								}
							}
						}
						
					/*} else {
						print("Record [identity=" + rec.identity + ", title=" + rec.__title 
								+ "] does not have tag value for [" + tcDimContentItem._id + "]");
					}*/
					
				});
				
				//print("Anomalies......");
				//printjson(anomalies);
				
				// flatten anomalies record
				var flatRecords = [];
				var keys = ['recordIdentity', 'recordTitle', 'recordType', 'contentItemIdentity', 'contentItemTitle', 'contentType', tcDimContentItem.label];
				
				anomalies.forEach(function(anomaly) {
					
					var flatRec = {
						recordIdentity: anomaly.record.identity,
						recordTitle: anomaly.record.title,
						recordType: anomaly.record.containerLabel
					};
					
					setDimFlagOnFlatRecord(anomaly.record.allDimValues, anomaly.record.dimValues, flatRec, keys);
					
					flatRecords.push(flatRec);
					
					anomaly.stackItems.forEach(function(asi) {
						
						var siFlatRecord = {
							contentItemIdentity: asi.identity,
							contentItemTitle: asi.title,
							contentType: asi.containerLabel
						};
						
						setDimFlagOnFlatRecord(anomaly.record.allDimValues, asi.dimValues, siFlatRecord);
						
						flatRecords.push(siFlatRecord);
					});
					
				});
				
				flatRecordsToCsv(flatRecords, keys);
				
			} else {
				print("Container [" + targetContainerQId + "] does not have a content stack property.");
			}
			
		} else {
			print("Content item not found for [" + dimQId + "]");
		}
			
		
	} else {
		print("No container definition found for [" + targetContainerQId + "]");
	}
	
} else {
	print("Content Template not found for tenant [" + tenantId + "]");
}
    

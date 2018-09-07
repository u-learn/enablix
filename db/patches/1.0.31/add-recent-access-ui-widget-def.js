/**************************************************************
Script to add recently access widget for all tenants.

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({
    "listDatabases": 1
}).databases;

var date = new Date();

var contentPackId = "ra-ui-widget-content-pack-id";
var contentPackDef = {
	"_class" : "com.enablix.core.domain.content.pack.ContentPack", 
    "title" : "Recently Accessed", 
    "type" : "RECENT_ACCESS_CONTENT", 
    "createdAt" : date, 
    "modifiedAt" : date, 
    "createdBy" : "system", 
    "createdByName" : "System", 
    "modifiedBy" : "system", 
    "modifiedByName" : "System", 
    "identity" : contentPackId
}

var raWidgetId = "ra-ui-widget-def-id";
var uiWidgetDef = { 
    "_class" : "com.enablix.core.domain.ui.ContentPackUIWidgetDef", 
    "title" : "Recently Accessed", 
    "type" : "CONTENT_PACK", 
    "contentPackIdentity" : contentPackId, 
    "createdAt" : date, 
    "modifiedAt" : date, 
    "createdBy" : "system", 
    "createdByName" : "System", 
    "modifiedBy" : "system", 
    "modifiedByName" : "System", 
    "identity" : raWidgetId
}

//Iterate through each database and get its collections.
dbs.forEach(function(database) {

  // only run the patch for client databases
  if (database.name.endsWith("_enablix")) {
      
  	db = db.getSiblingDB(database.name);
  	
  	
  	// insert content pack record
  	var contentPackColl = database.name == "system_enablix" ? "ref_ebx_content_pack" : "ebx_content_pack";
  	db[contentPackColl].remove({ identity: contentPackId });
  	db[contentPackColl].insert(contentPackDef)

  	
  	// insert ui widget definition
  	var uiWidgetDefColl = database.name == "system_enablix" ? "ref_ebx_ui_widget_def" : "ebx_ui_widget_def";
  	db[uiWidgetDefColl].remove({ identity: raWidgetId });
  	db[uiWidgetDefColl].insert(uiWidgetDef)
  	
  	
  	// add widget to user preference
  	/*var userPrefColl = database.name == "system_enablix" ? "ref_ebx_user_preference" : "ebx_user_preference";
  	
  	db[userPrefColl].update(
			{ key: "dashboard.widgets" },
			{ 
				$addToSet: { "config.widgetIds": raWidgetId },
				$setOnInsert: {
					"_class" : "com.enablix.core.domain.preference.UserPreference", 
				    "userId" : "system", 
				    "config.hideRecommendations" : false,
				    "createdAt" : date, 
				    "modifiedAt" : date, 
				    "createdBy" : "system", 
				    "createdByName" : "System", 
				    "modifiedBy" : "system", 
				    "modifiedByName" : "System", 
				    "identity" : "e832af4a-2879-45b7-8a0c-c7a655ef2193"
				}
			},
			{ upsert: true, multi: true }
		);*/
  	
  }
});

/**************************************************************
Set display order for quick links

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
        
    	var order = 0;
    	
        db.ebx_quick_link_category.find({}).snapshot().forEach(
        		function(cat) {
        			
        			db.ebx_quick_link_category.update(
        					{ 
        						"_id": cat._id
        					},
        					{
        						$set: {
        							"order": NumberInt(order)
        						}
        					}
        			);
        			
        			order++;
        		}
        );
        
        // set the counter value
        db.ebx_counter.update({"_id": "ebx_quick_link_category"}, { $set: { "currentValue" : NumberInt(order)} });
        
        order = 0;
        db.ebx_quick_links.find({}).snapshot().forEach(
        		function(lnk) {
        			
        			db.ebx_quick_links.update(
        					{ 
        						"_id": lnk._id
        					},
        					{
        						$set: {
        							"order": NumberInt(order)
        						}
        					}
        			);
        			
        			order++;
        		}
        );
        
        db.ebx_counter.update({"_id": "ebx_quick_links"}, { $set: { "currentValue" : NumberInt(order)} });
    }
});

/**************************************************************
Set display order for recommendations

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
    	
        db.ebx_recommendation.find({}).snapshot().forEach(
        		function(reco) {
        			
        			db.ebx_recommendation.update(
        					{ 
        						"_id": reco._id
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
        db.ebx_counter.update({"_id": "ebx_recommendation"}, { $set: { "currentValue" : NumberInt(order)} });
        
    }
});

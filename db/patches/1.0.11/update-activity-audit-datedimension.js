/**********************************************************************************************
Script to update the activity audit table to add date dimensionfor each entries

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/
Date.prototype.getDayOfTheYear = function(){
    var start = new Date(this.getFullYear(), 0, 0);
    var diff = this - start;
    var oneDay = 1000 * 60 * 60 * 24;
    var day = Math.floor(diff / oneDay);
    return day;
}

Date.prototype.getWeekOfMonth = function() {
  var firstWeekday = new Date(this.getFullYear(), this.getMonth(), 1).getDay();
  var offsetDate = this.getDate() + firstWeekday - 1;
  return Math.floor(offsetDate / 7)+1;
} 

Date.prototype.getWeekOfYear = function() {
    var onejan = new Date(this.getFullYear(),0,1);
    var millisecsInDay = 86400000;
    return Math.ceil((((this - onejan) /millisecsInDay) + onejan.getDay()+1)/7);
};

Date.prototype.getDayOfWeek = function() {
    var day = this.getDay()+7;
    if(day > 7) {
      day = day % 7;
    }
    return day;
};

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

	
        db.ebx_activity_audit.find({}).snapshot().forEach(
        		function(activityAudit) {
				var date = new Date(activityAudit.createdAt);
        			var dateDimension = {};

				dateDimension.dayOfWeek = NumberInt(date.getDayOfWeek());
				dateDimension.dayOfMonth =  NumberInt(date.getDate());
				dateDimension.dayOfYear =  NumberInt(date.getDayOfTheYear());
				dateDimension.weekOfMonth =  NumberInt(date.getWeekOfMonth());
				dateDimension.weekOfYear =  NumberInt(date.getWeekOfYear());
				dateDimension.monthOfYear =  NumberInt(date.getMonth()+1);
				dateDimension.year =  NumberInt(date.getFullYear());

        			db.ebx_activity_audit.update(
        					{ 
        						"_id": activityAudit._id
        					},
        					{
        						$set: {
        							"dateDimension":dateDimension
        						}
        					}
        			);
        			
        		}
        );
    }
});

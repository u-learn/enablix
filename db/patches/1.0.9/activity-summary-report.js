/**************************************************************
Script to update the search activity metric implementation data and delete the content access via weekly digest from the activity metric configuration table

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

// config data for the metric docs
var metricDocs = [
{
    "_id" : ObjectId("58ed94c5d3232f48251a1298"),
    "_class" : "com.enablix.core.domain.report.activitymetric.ActivityMetricConfig",
    "metricCode" : "ACTMETRIC9",
    "metricName" : "Searches",
    "startDate" : ISODate("2017-04-12T10:44:59.644Z"),
    "nextRunDate" : ISODate("2017-04-18T14:42:00.006Z"),
    "createdAt" : ISODate("2017-04-14T08:12:40.483Z"),
    "modifiedAt" : ISODate("2017-05-09T06:07:00.012Z"),
    "createdBy" : "system",
    "createdByName" : "System",
    "modifiedBy" : "system",
    "modifiedByName" : "System",
    "identity" : "ff09ec45-4ea4-4913-86b9-55a76d4d8809"
}
];

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
        db.ebx_activity_metric_config.remove({"metricCode":"ACTMETRIC10"});
        db.ebx_activity_metric_config.insert(metricDocs);
    }
});

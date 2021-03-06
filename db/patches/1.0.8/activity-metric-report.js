/**************************************************************
Script to insert data into activity metric configuration table

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

// config data for the metric docs
var metricDocs = [{
    "_id" : ObjectId("58ed94c5d3232f48251a1388"),
    "_class" : "com.enablix.core.domain.report.activitymetric.ActivityMetricConfig",
    "metricCode" : "ACTMETRIC2",
    "metricName" : "Number of Logins",
    "startDate" : ISODate("2017-04-12T10:44:59.644Z"),
    "nextRunDate" : ISODate("2017-04-18T14:42:00.006Z"),
    "createdAt" : ISODate("2017-04-14T08:12:40.483Z"),
    "modifiedAt" : ISODate("2017-04-18T14:42:00.031Z"),
    "createdBy" : "system",
    "createdByName" : "System",
    "modifiedBy" : "system",
    "modifiedByName" : "System",
    "identity" : "ff09ec45-4ea4-4913-86b9-55a76d4d8805"
},
{
    "_id" : ObjectId("58ed94c5d3232f48251a1389"),
    "_class" : "com.enablix.core.domain.report.activitymetric.ActivityMetricConfig",
    "metricCode" : "ACTMETRIC3",
    "metricName" : "Number of Distinct Logins",
    "startDate" : ISODate("2017-04-12T10:44:59.644Z"),
    "nextRunDate" : ISODate("2017-04-18T14:42:00.006Z"),
    "createdAt" : ISODate("2017-04-14T08:12:40.483Z"),
    "modifiedAt" : ISODate("2017-04-18T14:42:00.059Z"),
    "createdBy" : "system",
    "createdByName" : "System",
    "modifiedBy" : "system",
    "modifiedByName" : "System",
    "identity" : "ff09ec45-4ea4-4913-86b9-55a76d4d8805"
},
{
    "_id" : ObjectId("58ed94c5d3232f48251a1371"),
    "_class" : "com.enablix.core.domain.report.activitymetric.ActivityMetricConfig",
    "metricCode" : "ACTMETRIC4",
    "metricName" : "Content Add",
    "startDate" : ISODate("2017-04-12T10:44:59.644Z"),
    "nextRunDate" : ISODate("2017-04-18T14:42:00.006Z"),
    "createdAt" : ISODate("2017-04-14T08:12:40.483Z"),
    "modifiedAt" : ISODate("2017-04-18T14:42:00.091Z"),
    "createdBy" : "system",
    "createdByName" : "System",
    "modifiedBy" : "system",
    "modifiedByName" : "System",
    "identity" : "ff09ec45-4ea4-4913-86b9-55a76d4d8805"
},
{
    "_id" : ObjectId("58ed94c5d3232f48251a1372"),
    "_class" : "com.enablix.core.domain.report.activitymetric.ActivityMetricConfig",
    "metricCode" : "ACTMETRIC5",
    "metricName" : "Content Updates",
    "startDate" : ISODate("2017-04-12T10:44:59.644Z"),
    "nextRunDate" : ISODate("2017-04-18T14:42:00.006Z"),
    "createdAt" : ISODate("2017-04-14T08:12:40.483Z"),
    "modifiedAt" : ISODate("2017-04-18T14:42:00.128Z"),
    "createdBy" : "system",
    "createdByName" : "System",
    "modifiedBy" : "system",
    "modifiedByName" : "System",
    "identity" : "ff09ec45-4ea4-4913-86b9-55a76d4d8805"
},
{
    "_id" : ObjectId("58ed94c5d3232f48251a1373"),
    "_class" : "com.enablix.core.domain.report.activitymetric.ActivityMetricConfig",
    "metricCode" : "ACTMETRIC6",
    "metricName" : "Content Access",
    "startDate" : ISODate("2017-04-12T10:44:59.644Z"),
    "nextRunDate" : ISODate("2017-04-18T14:42:00.006Z"),
    "createdAt" : ISODate("2017-04-14T08:12:40.483Z"),
    "modifiedAt" : ISODate("2017-04-18T14:42:00.161Z"),
    "createdBy" : "system",
    "createdByName" : "System",
    "modifiedBy" : "system",
    "modifiedByName" : "System",
    "identity" : "ff09ec45-4ea4-4913-86b9-55a76d4d8805"
},
{
    "_id" : ObjectId("58ed94c5d3232f48251a1374"),
    "_class" : "com.enablix.core.domain.report.activitymetric.ActivityMetricConfig",
    "metricCode" : "ACTMETRIC7",
    "metricName" : "Content Preview",
    "startDate" : ISODate("2017-04-12T10:44:59.644Z"),
    "nextRunDate" : ISODate("2017-04-18T14:42:00.006Z"),
    "createdAt" : ISODate("2017-04-14T08:12:40.483Z"),
    "modifiedAt" : ISODate("2017-04-18T14:42:00.196Z"),
    "createdBy" : "system",
    "createdByName" : "System",
    "modifiedBy" : "system",
    "modifiedByName" : "System",
    "identity" : "ff09ec45-4ea4-4913-86b9-55a76d4d8805"
},
{
    "_id" : ObjectId("58ed94c5d3232f48251a1375"),
    "_class" : "com.enablix.core.domain.report.activitymetric.ActivityMetricConfig",
    "metricCode" : "ACTMETRIC8",
    "metricName" : "Content Download",
    "startDate" : ISODate("2017-04-12T10:44:59.644Z"),
    "nextRunDate" : ISODate("2017-04-18T14:42:00.006Z"),
    "createdAt" : ISODate("2017-04-14T08:12:40.483Z"),
    "modifiedAt" : ISODate("2017-04-18T14:42:00.229Z"),
    "createdBy" : "system",
    "createdByName" : "System",
    "modifiedBy" : "system",
    "modifiedByName" : "System",
    "identity" : "ff09ec45-4ea4-4913-86b9-55a76d4d8805"
},
{
    "_id" : ObjectId("58ed94c5d3232f48251a1379"),
    "_class" : "com.enablix.core.domain.report.activitymetric.ActivityMetricConfig",
    "metricCode" : "ACTMETRIC10",
    "metricName" : "Content Access via Weekly Digest",
    "startDate" : ISODate("2017-04-12T10:44:59.644Z"),
    "nextRunDate" : ISODate("2017-04-18T14:42:00.006Z"),
    "createdAt" : ISODate("2017-04-14T08:12:40.483Z"),
    "modifiedAt" : ISODate("2017-04-18T14:42:00.259Z"),
    "createdBy" : "system",
    "createdByName" : "System",
    "modifiedBy" : "system",
    "modifiedByName" : "System",
    "identity" : "ff09ec45-4ea4-4913-86b9-55a76d4d8805"
}];

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
        db.ebx_activity_metric_config.remove({});
        db.ebx_activity_metric_config.insert(metricDocs);
    }
});
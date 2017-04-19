/**************************************************************
Script to insert data into activity metric configuration table

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> --eval 'arg1=<path>' <path>/<script-file-name>

Example : mongo --eval 'arg1="/home/amitkumar/github-repository/enablix/db/patches/1.0.7/"' /home/amitkumar/github-repository/enablix/db/patches/1.0.7/activity-metric-report.js
****************************************************************/
// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({
    "listDatabases": 1
}).databases;

//var currentPath = '$1';
load(arg1+'/metricConfigurations.js');

//Iterate through each database and get its collections.
dbs.forEach(function(database) {

    // only run the patch for client databases
    if (database.name.endsWith("_enablix") &&
        database.name != "system_enablix") {
        db = db.getSiblingDB(database.name);    
        db.ebx_activity_metrictestt.insert(metricDocs);
    }
});

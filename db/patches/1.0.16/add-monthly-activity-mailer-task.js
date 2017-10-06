/**************************************************************
Script to insert config for scheduler and monthly activity report mailer task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "activity-metric-mailer-task-identity", 
    "name" : "Monthly Activity Metric Report", 
    "taskId" : "activity-metric-mailer-task",
    "tenantFilter" : ["haystax", "eoriginal"]
});

db.ebx_scheduler_config.insert({ 
    "identity" : "9am-est-5-monthly-scheduler", 
    "name" : "9 AM EST 5th of every month scheduler ", 
    "cronExpression" : "0 0 13 6 1/1 ?", 
    "tasksToExecute" : [
        "activity-metric-mailer-task-identity"
    ]
});


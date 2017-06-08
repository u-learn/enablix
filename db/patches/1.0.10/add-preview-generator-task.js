/**************************************************************
Script to generate preview files for existing documents

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "preview-generator-task-identity", 
    "name" : "Doc Preview Generator Task", 
    "taskId" : "preview-generator-task"
});

db.ebx_scheduler_config.insert({ 
    "identity" : "one-time-scheduler-06082017-0700", 
    "name" : "One Time 06/08/2017 07:00 GMT", 
    "cronExpression" : "0 0 7 8 6 2017", 
    "tasksToExecute" : [
        "preview-generator-task-identity"
    ]
});
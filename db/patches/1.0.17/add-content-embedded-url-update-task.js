/**************************************************************
Script to insert config for Content Emdedded URL update task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "content-embedded-urls-update-identity", 
    "name" : "Content Embedded URLs Update Task", 
    "taskId" : "content-embedded-urls-update-task",
});



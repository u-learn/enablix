/**************************************************************
Script to insert config for Doc Emdedd Html task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "doc-embed-html-task-identity", 
    "name" : "Doc Embedd Html Task", 
    "taskId" : "doc-embed-html-task",
});



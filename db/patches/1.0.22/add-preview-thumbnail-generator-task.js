/**************************************************************
Script to generate preview thumbnail files for existing documents

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "preview-thumbnail-gen-task-identity", 
    "name" : "Doc Preview Thumbnail Generator Task", 
    "taskId" : "preview-thumbnail-gen-task",
});
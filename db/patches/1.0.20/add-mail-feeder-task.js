/**************************************************************

Script to insert config for mail content feeder task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "mail-feeder-task-identity", 
    "name" : "Mail Content Feeder", 
    "taskId" : "mail-feeder-task",
    "tenantFilter" : ["eoriginal"]
});

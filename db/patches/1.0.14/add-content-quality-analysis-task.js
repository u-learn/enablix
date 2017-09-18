/**************************************************************

Script to insert config one time Content Quality Analysis task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "content-quality-analysis-task-identity", 
    "name" : "Content Quality Analysis", 
    "taskId" : "content-quality-analysis-task",
    "tenantFilter" : []
});


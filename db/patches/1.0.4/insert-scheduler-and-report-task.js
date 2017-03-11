/**************************************************************
Script to insert config for scheduler and content coverage calculator task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "content-coverage-summary-task-identity", 
    "name" : "Content Coverage Summary Task", 
    "taskId" : "content-coverage-calculator"
});

db.ebx_scheduler_config.insert({	
	"identity" : "daily-11-pm-est-scheduler", 
    "name" : "Daily 11 PM EST Scheduler", 
    "cronExpression" : "0 0 4 1/1 * ?", 
    "tasksToExecute" : [
		"content-coverage-summary-task-identity"
    ]
});


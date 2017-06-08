/**************************************************************
Script to insert config for scheduler and Wordpress Post Import Mailer task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "wp-imported-post-mailer-task-identity", 
    "name" : "Wordpress Post Import Mailer Task", 
    "taskId" : "wp-imported-post-mailer-task",
    "tenantFilter" : [
    	"haystax"
    ]
});

db.ebx_scheduler_config.insert({ 
    "identity" : "daily-1-am-est-scheduler", 
    "name" : "Daily 1 AM EST Scheduler", 
    "cronExpression" : "0 0 5 1/1 * ?", 
    "tasksToExecute" : [
        "wp-imported-post-mailer-task-identity"
    ]
});


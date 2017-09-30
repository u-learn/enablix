/**************************************************************
Script to insert config for scheduler and Weekly digest mailer task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "weekly-digest-mailer-identity", 
    "name" : "Weekly Digest Mailer Task", 
    "taskId" : "weekly-digest-mailer",
});

db.ebx_scheduler_config.insert({ 
    "identity" : "weekly-digest-mail-scheduler", 
    "name" : "Weekly Digest Mail Scheduler", 
    "cronExpression" : "0 0 11 ? * TUE", 
    "tasksToExecute" : [
        "weekly-digest-mailer-identity"
    ]
});


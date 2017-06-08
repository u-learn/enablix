/**************************************************************
Script to insert config for scheduler and Wordpress Post Feeder task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "wp-post-feeder-task-identity", 
    "name" : "Wordpress Post Feeder Task", 
    "taskId" : "wp-post-feeder-task",
    "tenantFilter" : [
    	"haystax"
    ]
});

db.ebx_scheduler_config.update(
		{ identity: 'daily-11-pm-est-scheduler' },
		{ $addToSet: { tasksToExecute: { $each: ["wp-post-feeder-task-identity"] } } }
    );

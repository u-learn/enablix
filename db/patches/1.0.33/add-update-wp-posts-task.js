/**************************************************************
Script to insert config for Update Wordpress imported blogs task

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({
	"identity" : "update-wp-post-feeder-task-identity", 
    "name" : "Update Wordpress Post Feeder Task", 
    "taskId" : "wp-post-feeder-task",
    "parameters": {
    	"updateExisting": true,
    	"userzoom|lastrun": ISODate("2018-09-11T04:35:01.733+0000")
    },
    "tenantFilter" : [
    	"userzoom"
    ]
});



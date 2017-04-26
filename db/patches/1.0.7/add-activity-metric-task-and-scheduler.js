/**************************************************************
Script to add activity metric calculator task and also create a 
1 AM GMT scheduler to run that task.

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

// Switch to system database
db = db.getSiblingDB("system_enablix");

db.ebx_task_config.insert({ 
    "identity" : "activity-metric-calculator-identity", 
    "name" : "Activity Metric Calculator Task", 
    "taskId" : "activity-metric-calculator"
});

db.ebx_scheduler_config.insert({ 
    "identity" : "daily-1-am-gmt-scheduler", 
    "name" : "Daily 1 AM GMT Scheduler", 
    "cronExpression" : "0 0 1 1/1 * ?", 
    "tasksToExecute" : [
        "activity-metric-calculator-identity"
    ]
});

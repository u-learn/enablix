/**************************************************************
Script to create config required by Wordpress Analyser for Haystax

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

var date = new Date();
var reportId = "sp-content-coverage-report";
var cannedRptPref = 
	{
	    "_class" : "com.enablix.core.domain.preference.UserPreference", 
	    "userId" : "system", 
	    "key" : "reports.canned.list", 
	    "config" : {
	        "reports" : [
	            {
	                "id" : reportId, 
	                "name" : "Sales Playbook Coverage", 
	                "baseReportId" : "content-coverage-report", 
	                "heading" : "Sales Playbook Coverage", 
	                "options" : {
	                    "fillEmptyAsZero" : true, 
	                    "stopDrilldown" : true
	                }, 
	                "filters" : [
	                    {
	                        "id" : "contentQIdIn", 
	                        "type" : "pre-defined", 
	                        "value" : [
	                            {
	                                "id" : "salesplaybook", 
	                                "label" : "Sales Playbooks"
	                            }
	                        ]
	                    }
	                ]
	            }
	        ]
	    }, 
	    "createdAt" : date, 
	    "modifiedAt" : date, 
	    "createdBy" : "system", 
	    "createdByName" : "System", 
	    "modifiedBy" : "system", 
	    "modifiedByName" : "System", 
	    "identity" : "6aafce64-9d6f-4116-a0cb-13c3cb384af1"
		
	};

// Switch to haystax database
db = db.getSiblingDB("eoriginal_enablix");

var coll = db.getCollection("ebx_user_preference");
coll.insert(cannedRptPref);

// insert the corresponding report view permission
db.ebx_role.update(
		{ _id: 'contentAdmin' },
		{ $addToSet: { permissions: { $each: ["VIEW_REPORT-" + reportId] } } }
    );
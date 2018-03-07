/**************************************************************
Script to create reference collections to be used during new tenant setup

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

var roles = [
	{ 
	    "_id" : "contentAdmin", 
	    "_class" : "com.enablix.core.domain.security.authorization.Role", 
	    "identity" : "contentAdmin", 
	    "roleName" : "Administrator", 
	    "permissions" : [
	    	"VIEW_STUDIO", 
	        "VIEW_REF_DATA", 
	        "VIEW_PORTAL", 
	        "VIEW_RECENT_CONTENT", 
	        "MANAGE_CONTENT_REQUEST", 
	        "SHARE_VIA_EMAIL", 
	        "SHARE_VIA_SLACK", 
	        "MANAGE_INTEGRATIONS", 
	        "VIEW_REPORT-activity-metric-calculator", 
	        "VIEW_REPORT-content-coverage-report", 
	        "VIEW_REPORTS", 
	        "VIEW_REPORT-activity-trend-calculator", 
	        "DOCSTORE_DIRECT_ACCESS"
	    ]
	},
	{ 
	    "_id" : "portalUser", 
	    "_class" : "com.enablix.core.domain.security.authorization.Role", 
	    "identity" : "portalUser", 
	    "roleName" : "Consumer", 
	    "permissions" : [
	    	"VIEW_PORTAL", 
	        "VIEW_RECENT_CONTENT", 
	        "SUGGEST_CONTENT", 
	        "SHARE_VIA_EMAIL", 
	        "SHARE_VIA_SLACK", 
	        "DOCSTORE_DIRECT_ACCESS"
	    ]
	}];

// Switch to system database
db = db.getSiblingDB("system_enablix");

var coll = db.getCollection("ref_ebx_role");
roles.forEach(function(role) {
	coll.insert(role);
});

/**************************************************************
Script to create a Partner User role for Haystax

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

var partnerUserRole = { 
	    "_id" : "partnerUser", 
	    "_class" : "com.enablix.core.domain.security.authorization.Role", 
	    "identity" : "partnerUser", 
	    "roleName" : "Partner User", 
	    "permissions" : [
	        "VIEW_PORTAL", 
	        "VIEW_RECENT_CONTENT"
	    ]
	};

// Switch to haystax database
db = db.getSiblingDB("haystax_enablix");

db.ebx_role.insert(partnerUserRole);

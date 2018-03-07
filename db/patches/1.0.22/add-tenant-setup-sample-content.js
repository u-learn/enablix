/**************************************************************
Script to create sample content records for new tenant

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

var contentList = [
	{ 
	    "contentQId" : "product", 
	    "record" : {
	        "identity" : "9ab4a019-fa39-4eb1-ad0e-b358d37ac7d4", 
	        "name" : "Enablix", 
	        "shortName" : "Enablix"
	    }
	},
	{ 
	    "contentQId" : "battlecard", 
	    "record" : {
	        "title" : "Enablix Battle Card", 
	        "products" : [
	            {
	                "id" : "9ab4a019-fa39-4eb1-ad0e-b358d37ac7d4", 
	                "label" : "Enablix"
	            }
	        ], 
	        "file" : {
	            "location" : "Enablix-Battlecard.pdf", 
	            "contentType" : "application/pdf"
	        }, 
	        "identity" : "1eeddbf9-55cf-4035-99ce-85bb19bb8015", 
	        "desc" : "This is a Enablix Sales Enablement Battle Card targeted for tier 1 organizations."
	    }
	},
	{ 
	    "contentQId" : "blog", 
	    "record" : {
	        "identity" : "610bac7f-fa98-4020-8bf3-71a56c22ecba", 
	        "title" : "Tips for managing Marketing and Sales Content on Dropbox", 
	        "url" : "https://enablix.wordpress.com/2017/06/21/tips-for-managing-marketing-and-sales-content-on-dropbox/", 
	        "products" : [
	            {
	                "id" : "9ab4a019-fa39-4eb1-ad0e-b358d37ac7d4", 
	                "label" : "Enablix"
	            }
	        ]
	    }
	},
	{ 
	    "contentQId" : "blog", 
	    "record" : {
	        "identity" : "ef894991-634e-4503-9171-7c4c651ab2f3", 
	        "title" : "Does sales know what is published on company’s website?", 
	        "url" : "https://enablix.wordpress.com/2018/01/31/does-sales-know-what-is-published-on-companys-website/", 
	        "products" : [
	            {
	                "id" : "9ab4a019-fa39-4eb1-ad0e-b358d37ac7d4", 
	                "label" : "Enablix"
	            }
	        ]
	    }
	}];

// Switch to system database
db = db.getSiblingDB("system_enablix");

var coll = db.getCollection("ebx_sample_content");
contentList.forEach(function(content) {
	coll.insert(content);
});

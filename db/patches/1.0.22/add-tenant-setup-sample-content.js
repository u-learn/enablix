/**************************************************************
Script to create sample content records for new tenant

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

var contentList = [
	{ 
	    "contentQId" : "solution", 
	    "record" : {
	        "identity" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        "name" : "Sales Enablement", 
	        "shortName" : "Sales Enablement"
	    }
	},
	{ 
	    "contentQId" : "solution", 
	    "record" : {
	        "identity" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        "name" : "Sales Portal", 
	        "shortName" : "Sales Portal"
	    }
	},
	{ 
	    "contentQId" : "solution", 
	    "record" : {
	        "identity" : "50c3a610-c4f8-4e1e-aec9-7eaa8ead5d73", 
	        "name" : "Partner Enablement", 
	        "shortName" : "Partner Enablement"
	    }
	},
	{ 
	    "contentQId" : "solution", 
	    "record" : {
	        "identity" : "3545e685-34e7-4744-ad85-e6318171b8d5", 
	        "name" : "Content Management", 
	        "shortName" : "Content Management"
	    }
	},
	{ 
	    "contentQId" : "integration", 
	    "record" : {
	        "identity" : "2283aa0c-b8c7-4b03-b5f6-75f65107f813", 
	        "name" : "HubSpot", 
	        "shortName" : "HubSpot"
	    }
	},
	{ 
	    "contentQId" : "integration", 
	    "record" : {
	        "identity" : "f630adc7-3191-4035-be7f-2899349947fc", 
	        "name" : "Salesforce", 
	        "shortName" : "Salesforce"
	    }
	},
	{ 
	    "contentQId" : "integration", 
	    "record" : {
	        "identity" : "3a8365ab-9867-4f2e-aac3-4cbf498fdbf5", 
	        "name" : "Salesloft", 
	        "shortName" : "Salesloft"
	    }
	},
	{ 
	    "contentQId" : "integration", 
	    "record" : {
	        "identity" : "c75eab5a-6765-407b-8b6e-21c966661dfc", 
	        "name" : "YouTube", 
	        "shortName" : "YouTube"
	    }
	},
	{ 
	    "contentQId" : "integration", 
	    "record" : {
	        "identity" : "f1b700c7-fd50-4ad3-bda9-1aba070c740c", 
	        "name" : "Google Drive", 
	        "shortName" : "Google Drive"
	    }
	},
	{ 
	    "contentQId" : "integration", 
	    "record" : {
	        "identity" : "031afe45-a8b1-4c7f-82ae-e0f0fe010aaa", 
	        "name" : "Sharepoint", 
	        "shortName" : "Sharepoint"
	    }
	},
	{ 
	    "contentQId" : "integration", 
	    "record" : {
	        "identity" : "5bffbe16-33a6-4093-be6a-62aaa1ca07f4", 
	        "name" : "Dropbox", 
	        "shortName" : "Dropbox"
	    }
	},
	{ 
	    "contentQId" : "topic", 
	    "record" : {
	        "identity" : "cdd36b31-c669-482d-950a-c2ab064a8129", 
	        "name" : "Search", 
	        "shortName" : "Search"
	    }
	},
	{ 
	    "contentQId" : "topic", 
	    "record" : {
	        "identity" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        "name" : "Sales Playbooks", 
	        "shortName" : "Sales Playbooks"
	    }
	},
	{ 
	    "contentQId" : "topic", 
	    "record" : {
	        "identity" : "39db9637-3dab-4605-bd02-ac9e9e55866d", 
	        "name" : "Customer Success", 
	        "shortName" : "Customer Success"
	    }
	},
	{ 
	    "contentQId" : "topic", 
	    "record" : {
	        "identity" : "a4b1d131-53c1-40e9-a083-8abab4ead902", 
	        "name" : "Guided Selling", 
	        "shortName" : "Guided Selling"
	    }
	},
	{ 
	    "contentQId" : "product", 
	    "record" : {
	        "identity" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        "name" : "Knowledge Management", 
	        "shortName" : "Knowledge Management"
	    }
	},
	{ 
	    "contentQId" : "product", 
	    "record" : {
	        "identity" : "ffbd6690-6625-48c0-b5a4-e65d6f28db53", 
	        "name" : "Salesforce Plugin", 
	        "shortName" : "Salesforce Plugin"
	    }
	},
	{ 
	    "contentQId" : "product", 
	    "record" : {
	        "identity" : "025cd86e-42db-4f66-a8da-5dc0ad330655", 
	        "name" : "HubSpot Plugin", 
	        "shortName" : "HubSpot Plugin"
	    }
	},
	{ 
	    "contentQId" : "image", 
	    "record" : {
	    	"identity" : "c6f24f06-b0a4-42c8-b820-c5db40b3f90a",
	    	"title" : "Logo Large",
	        "desc" : "It is common to source your stock images from different sources. Furthermore you have your own high fidelity product images and screenshots that you regularly use to represent your product. Enablix can help you organize all your images in one place. This is a sample asset. Feel free to update or delete this asset.",
	        "products" : [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ],
	        "file" : {
	            "location" : "enablix_logo_big@2x.png", 
	            "contentType" : "image/png"
	        }
	    }
	},
	{ 
	    "contentQId" : "video", 
	    "record" : {
	    	"identity" : "d3b607d0-2b9b-4e94-8e5b-857bcbf5c17d",
	    	"title" : "9 Steps for Sales Enablement Success",
	        "desc" : "With increasing popularity of video content, organizations are investing thousands of dollars in this medium. Enablix helps organizations capture and organize their videos so that they are easy to access for your sales and other customer facing teams along with other relevant content. In this asset, we have captured a YouTube video. This is a sample asset. Feel free to update or delete this asset.",
	        "url" : "https://www.youtube.com/watch?v=j_a4daOZhyc",
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	},
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	},
	        	{ 
	        	    "id" : "50c3a610-c4f8-4e1e-aec9-7eaa8ead5d73", 
	        	    "label" : "Partner Enablement", 
	        	}
	        ],
	        "topics" : [
	        	{ 
	        	    "id" : "cdd36b31-c669-482d-950a-c2ab064a8129", 
	        	    "label" : "Search", 
	        	},
	        	{ 
	        	    "id" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        	    "label" : "Sales Playbooks", 
	        	},
	        	{ 
	        	    "id" : "39db9637-3dab-4605-bd02-ac9e9e55866d", 
	        	    "label" : "Customer Success", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	},
	        	{ 
	        	    "id" : "ffbd6690-6625-48c0-b5a4-e65d6f28db53", 
	        	    "label" : "Salesforce Plugin", 
	        	},
	        	{ 
	        	    "id" : "025cd86e-42db-4f66-a8da-5dc0ad330655", 
	        	    "label" : "HubSpot Plugin", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "importantslide", 
	    "record" : {
	    	"identity" : "fbd48ba4-4a52-4395-9ae0-034b6779c015",
	    	"title" : "Content Coverage",
	        "desc" : "A lot of our content is in slides. And every business has those important slides that relay their message and speak to their offerings. Enablix helps marketers, product owners and sales to maintain a trusted library of important slides. In this important slide, we highlight Enablix's ability to coverage different types of content types across multiple sources. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "Content Coverage.pptx", 
	            "contentType" : "application/vnd.openxmlformats-officedocument.presentationml.presentation",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	},
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	},
	        	{ 
	        	    "id" : "3545e685-34e7-4744-ad85-e6318171b8d5", 
	        	    "label" : "Content Management", 
	        	}
	        ],
	        "topics" : [
	        	{ 
	        	    "id" : "cdd36b31-c669-482d-950a-c2ab064a8129", 
	        	    "label" : "Search", 
	        	},
	        	{ 
	        	    "id" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        	    "label" : "Sales Playbooks", 
	        	},
	        	{ 
	        	    "id" : "39db9637-3dab-4605-bd02-ac9e9e55866d", 
	        	    "label" : "Customer Success", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "importantslide", 
	    "record" : {
	    	"identity" : "54e24a24-f3df-44d6-a70c-983c7f0814cb",
	    	"title" : "AI using Bayesian Networks",
	        "desc" : "A lot of our content is in slides. And every business has those important slides that relay their message and speak to their offerings. Enablix helps marketers, product owners and sales to maintain a trusted library of important slides. In this important slide, we show case the underlying model that drives user targeted content. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "Bayesian Networks.pptx", 
	            "contentType" : "application/vnd.openxmlformats-officedocument.presentationml.presentation",
	            "generatePreview" : false
	        },
	        "topics" : [
	        	{ 
	        	    "id" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        	    "label" : "Sales Playbooks", 
	        	},
	        	{ 
	        	    "id" : "a4b1d131-53c1-40e9-a083-8abab4ead902", 
	        	    "label" : "Guided Selling", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "proposal", 
	    "record" : {
	    	"identity" : "f7f58b85-eacf-4bfd-9794-a00155fff33f",
	    	"title" : "Enablix Proposal",
	        "desc" : "Proposals are regularly used by B2B companies to engage in enterprise opportunities. A lot of work goes into creating winning proposals. Therefore, it is expected to reuse that effort in other similar proposals. Enablix helps organizations manage proposals in one single location. This, in turn, helps sales reps to find and leverage winning proposals without losing productivity and compromising an opportunity. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "2018 Enablix Proposal.docx", 
	            "contentType" : "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	},
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	},
	        	{ 
	        	    "id" : "50c3a610-c4f8-4e1e-aec9-7eaa8ead5d73", 
	        	    "label" : "Partner Enablement", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "casestudy", 
	    "record" : {
	    	"identity" : "f7b0fbfc-e497-477d-997c-6599dba05336",
	    	"title" : "Customer Case Study",
	        "desc" : "Case Studies are vital marketing assets. Organizations spend lot of money on building effective case studies. Enablix organizes these case studies for easy access. This is one of our customer's study on how they benefitted from Enablix. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "201705 CustomerSuccess CaseStudy.pptx", 
	            "contentType" : "application/vnd.openxmlformats-officedocument.presentationml.presentation",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	},
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	},
	        	{ 
	        	    "id" : "3545e685-34e7-4744-ad85-e6318171b8d5", 
	        	    "label" : "Content Management", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "content", 
	    "record" : {
	    	"identity" : "64957873-4a71-42f2-bf0a-2b20cdcf18fd",
	    	"title" : "Content Catalog",
	        "desc" : "If there is a content asset for which you don't see a type in Enablix, you can contact us and we will add the new type for you free of cost. Otherwise, you can always add that asset as a generic content by selecting the Other Content category. This is Enablix's content catalog spreadsheet that we use to map business dimensions to content types and track them. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "Content Catalog.xlsx", 
	            "contentType" : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	},
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	},
	        	{ 
	        	    "id" : "3545e685-34e7-4744-ad85-e6318171b8d5", 
	        	    "label" : "Content Management", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "datasheet", 
	    "record" : {
	    	"identity" : "564a3fbb-ddb1-45d2-b7c6-293d418f0f1a",
	    	"title" : "Enablix Overview",
	        "desc" : "Datasheets are a commonly used content type in companies. This is a two page introductory data sheet on Enablix. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "Solution Brief.pdf", 
	            "contentType" : "application/pdf",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	},
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	},
	        	{ 
	        	    "id" : "3545e685-34e7-4744-ad85-e6318171b8d5", 
	        	    "label" : "Content Management", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "datasheet", 
	    "record" : {
	    	"identity" : "37afe17e-4712-4492-b59a-db8fe3172ff6",
	    	"title" : "Accelerate Pipeline",
	        "desc" : "Datasheets are a commonly used content type in companies. This data sheet discusses how Enablix helps organizations accelerate their sales pipeline. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "AcceleratePipeline.pdf", 
	            "contentType" : "application/pdf",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	}
	        ],
	        "topics" : [
	        	{ 
	        	    "id" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        	    "label" : "Sales Playbooks", 
	        	},
	        	{ 
	        	    "id" : "a4b1d131-53c1-40e9-a083-8abab4ead902", 
	        	    "label" : "Guided Selling", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "datasheet", 
	    "record" : {
	    	"identity" : "3c95edc8-bbaf-416e-8dc2-d20421410e16",
	    	"title" : "Automate Sales Plays",
	        "desc" : "Datasheets are a commonly used content type in companies. This data sheet discusses how marketing and sales management can automate sales plays with Enablix. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "SalesPlay.pdf", 
	            "contentType" : "application/pdf",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	}
	        ],
	        "topics" : [
	        	{ 
	        	    "id" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        	    "label" : "Sales Playbooks", 
	        	},
	        	{ 
	        	    "id" : "a4b1d131-53c1-40e9-a083-8abab4ead902", 
	        	    "label" : "Guided Selling", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	},
	        	{ 
	        	    "id" : "ffbd6690-6625-48c0-b5a4-e65d6f28db53", 
	        	    "label" : "Salesforce Plugin", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "presentation", 
	    "record" : {
	    	"identity" : "ae9d57a9-8f58-4327-be63-89d2e6feea84",
	    	"title" : "Enablix Sales Presentation",
	        "desc" : "Presentations are an important and highly used content type by sales teams. This is a standard sales presentation of Enablix. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "Sales Presentation.pptx", 
	            "contentType" : "application/vnd.openxmlformats-officedocument.presentationml.presentation",
	            "generatePreview" : false
	        },
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "presentation", 
	    "record" : {
	    	"identity" : "8c2cf9b9-1fc1-44c4-992f-08f4b33460b4",
	    	"title" : "Enablix and Salesforce Integration",
	        "desc" : "Presentations are an important content type. This is a sales presentation that discusses integration between Enablix and Salesforce through screenshots. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "Integration SF.pptx", 
	            "contentType" : "application/vnd.openxmlformats-officedocument.presentationml.presentation",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	}
	        ],
	        "integrations" : [
	        	{ 
	        	    "id" : "f630adc7-3191-4035-be7f-2899349947fc", 
	        	    "label" : "Salesforce", 
	        	}
	        ],
	        "topics" : [
	        	{ 
	        	    "id" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        	    "label" : "Sales Playbooks", 
	        	},
	        	{ 
	        	    "id" : "a4b1d131-53c1-40e9-a083-8abab4ead902", 
	        	    "label" : "Guided Selling", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	},
	        	{ 
	        	    "id" : "ffbd6690-6625-48c0-b5a4-e65d6f28db53", 
	        	    "label" : "Salesforce Plugin", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "presentation", 
	    "record" : {
	    	"identity" : "25484760-3167-4ad0-8180-9d388089ab5f",
	    	"title" : "Enablix and HubSpot CRM Integration",
	        "desc" : "Presentations are an important content type. This is a sales presentation that discusses integration between Enablix and HubSpot CRM. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "hubspot_sales_enablement.pdf", 
	            "contentType" : "application/pdf",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	}
	        ],
	        "integrations" : [
	        	{ 
	        	    "id" : "2283aa0c-b8c7-4b03-b5f6-75f65107f813", 
	        	    "label" : "HubSpot", 
	        	}
	        ],
	        "topics" : [
	        	{ 
	        	    "id" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        	    "label" : "Sales Playbooks", 
	        	},
	        	{ 
	        	    "id" : "a4b1d131-53c1-40e9-a083-8abab4ead902", 
	        	    "label" : "Guided Selling", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	},
	        	{ 
	        	    "id" : "025cd86e-42db-4f66-a8da-5dc0ad330655", 
	        	    "label" : "HubSpot Plugin", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "whitepaper", 
	    "record" : {
	    	"identity" : "8caab6ef-95d7-4701-a765-5894b59dc60f",
	    	"title" : "Defining, Supporting, and Managing Your Best Practices Sales Process with The Six Boxes Approach",
	        "desc" : "Organizations create several white paper and leverage external white papers for reference. This external white paper is captured as a URL in Enablix. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	        	"location" : "101039_SixBoxesSalesPerformance.pdf", 
	            "contentType" : "application/pdf",
	            "generatePreview" : false
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "integration", 
	    "record" : {
	        "identity" : "2d37ea1e-6547-41f6-8244-aa2557e5387e", 
	        "name" : "Wordpress", 
	        "shortName" : "Wordpress"
	    }
	},
	{ 
	    "contentQId" : "faq", 
	    "record" : {
	    	"identity" : "2eccee3a-2028-4003-86e1-c68560f2a4d2",
	    	"title" : "How is Enablix different than other cloud storage platforms?",
	        "desc" : "Enablix is not an alternative to other cloud storage platforms. On the contrary we integrate with our customer's cloud storage platform and store all the file content assets on that platform. Existing cloud storage platforms, by themselves, are limiting when it comes to content organization capabilities. Furthemore, as content is increasing moving to the web, not all your knowledge is stored in files. Enablix helps you bridge that gap by supporting files, text and web sources to manage your organization's knowledge base. It provides your employees a centralized, easy-to-use interface, to access trusted content. This is a sample asset. Feel free to update or delete this asset.",
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	},
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	},
	        	{ 
	        	    "id" : "50c3a610-c4f8-4e1e-aec9-7eaa8ead5d73", 
	        	    "label" : "Partner Enablement", 
	        	}
	        ],
	        "topics" : [
	        	{ 
	        	    "id" : "cdd36b31-c669-482d-950a-c2ab064a8129", 
	        	    "label" : "Search", 
	        	},
	        	{ 
	        	    "id" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        	    "label" : "Sales Playbooks", 
	        	},
	        	{ 
	        	    "id" : "39db9637-3dab-4605-bd02-ac9e9e55866d", 
	        	    "label" : "Customer Success", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	},
	        	{ 
	        	    "id" : "ffbd6690-6625-48c0-b5a4-e65d6f28db53", 
	        	    "label" : "Salesforce Plugin", 
	        	},
	        	{ 
	        	    "id" : "025cd86e-42db-4f66-a8da-5dc0ad330655", 
	        	    "label" : "HubSpot Plugin", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "blog", 
	    "record" : {
	        "identity" : "a45a49ab-044e-4c5c-9a85-743b979d5f6d", 
	        "title" : "Why storing sales content inside CRM does not pay off?", 
	        "desc" : "This is an example of an internal Blog URL captured on Enablix. One can also integrate Enablix with your website's content management system (e.g., Wordpress) so that any new blog published on your website is automatically captured on Enablix. This is a sample asset. Feel free to update this asset with your own blog entry or delete it.",
	        "url" : "https://enablix.wordpress.com/2018/03/12/why-storing-sales-content-inside-crm-does-not-pay-off/", 
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	}
	        ],
	        "products" : [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ],
	        "integrations" : [
	        	{ 
	        	    "id" : "2283aa0c-b8c7-4b03-b5f6-75f65107f813", 
	        	    "label" : "HubSpot", 
	        	},
	        	{ 
	        	    "id" : "f630adc7-3191-4035-be7f-2899349947fc", 
	        	    "label" : "Salesforce", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "image", 
	    "record" : {
	    	"identity" : "955f1c53-291a-40e9-a0f3-21cf82dbe5cf",
	    	"title" : "Website Home Image",
	        "desc" : "It is common to source your stock images from different sources. Furthermore you have your own high fidelity product images and screenshots that you regularly use to represent your product. Enablix can help you organize all your images in one place. This is a sample asset. Feel free to update or delete this asset.",
	        "products" : [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ],
	        "file" : {
	            "location" : "platform_with_shadow_asset1@2x.png", 
	            "contentType" : "image/png"
	        }
	    }
	},
	{ 
	    "contentQId" : "blog", 
	    "record" : {
	        "identity" : "5046dd49-258a-497d-a21d-4f1b8001887b", 
	        "title" : "Why Content in Sales Enablement Covers Much More Than \"Marketing Content\"",
	        "desc" : "This content asset is an external Blog URL captured on Enablix. This is a sample asset. Feel free to update or delete this asset.",
	        "url" : "https://www.linkedin.com/pulse/why-content-sales-enablement-covers-much-more-tamara-schenk/?utm_campaign=Twitter&utm_content=61411833&utm_medium=social&utm_source=twitter", 
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	}
	        ],
	        "products" : [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "video", 
	    "record" : {
	    	"identity" : "5da4a061-0358-45fa-aec2-463f164c9194",
	    	"title" : "Enablix Intro Video",
	        "desc" : "With increasing popularity of video content, organizations are investing thousands of dollars in this medium. Enablix helps organizations capture and organize their videos so that they are easy to access for your sales and other customer facing teams along with other relevant content. In this asset, we are sharing our initial intro video that we hosted on Wistia. This is a sample asset. Feel free to update or delete this asset.",
	        "url" : "https://enablix.wistia.com/medias/5ti02nh9qq",
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	},
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	},
	        	{ 
	        	    "id" : "50c3a610-c4f8-4e1e-aec9-7eaa8ead5d73", 
	        	    "label" : "Partner Enablement", 
	        	}
	        ],
	        "topics" : [
	        	{ 
	        	    "id" : "cdd36b31-c669-482d-950a-c2ab064a8129", 
	        	    "label" : "Search", 
	        	},
	        	{ 
	        	    "id" : "cd6ff226-e53f-4a9a-b974-1162c143916e", 
	        	    "label" : "Sales Playbooks", 
	        	},
	        	{ 
	        	    "id" : "39db9637-3dab-4605-bd02-ac9e9e55866d", 
	        	    "label" : "Customer Success", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	},
	        	{ 
	        	    "id" : "ffbd6690-6625-48c0-b5a4-e65d6f28db53", 
	        	    "label" : "Salesforce Plugin", 
	        	},
	        	{ 
	        	    "id" : "025cd86e-42db-4f66-a8da-5dc0ad330655", 
	        	    "label" : "HubSpot Plugin", 
	        	}
	        ],
	        "integrations" : [
	        	{ 
	        	    "id" : "2283aa0c-b8c7-4b03-b5f6-75f65107f813", 
	        	    "label" : "HubSpot", 
	        	},
	        	{ 
	        	    "id" : "f630adc7-3191-4035-be7f-2899349947fc", 
	        	    "label" : "Salesforce", 
	        	},
	        	{ 
	        	    "id" : "f1b700c7-fd50-4ad3-bda9-1aba070c740c", 
	        	    "label" : "Google Drive", 
	        	},
	        	{ 
	        	    "id" : "031afe45-a8b1-4c7f-82ae-e0f0fe010aaa", 
	        	    "label" : "Sharepoint", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "casestudy", 
	    "record" : {
	    	"identity" : "752ee626-bb5a-489e-85c0-4477c2a2c02d",
	    	"title" : "Insight: Sales Content Engagement",
	        "desc" : "Case Studies are vital marketing assets. Organizations spend lot of money on building effective case studies. Enablix organizes these case studies for easy access. This case study highlights sales engagement for an entire year for one of Enablix's customer. This is a sample asset. Feel free to update or delete this asset.",
	        "file" : {
	            "location" : "content_engagement.pdf", 
	            "contentType" : "application/pdf",
	            "generatePreview" : true
	        },
	        "solutions" : [
	        	{
	        		"id" : "0072f55a-6170-4e25-8618-0d69b8fd4654", 
	        		"label" : "Sales Enablement", 
	        	},
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	},
	        	{ 
	        	    "id" : "3545e685-34e7-4744-ad85-e6318171b8d5", 
	        	    "label" : "Content Management", 
	        	}
	        ],
	        "products": [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "integration", 
	    "record" : {
	        "identity" : "40ade964-5b5e-4c00-b1e5-e3d9deea81bd", 
	        "name" : "Slack", 
	        "shortName" : "Slack"
	    }
	},
	{ 
	    "contentQId" : "faq", 
	    "record" : {
	    	"identity" : "808e8975-4a06-4988-b2f2-6827804c22e0",
	    	"title" : "How much does it cost to use Enablix?",
	        "desc" : "After the trial period is over, Enablix costs $10 per user per month. This is a sample asset. Feel free to update or delete this asset.",
	        "products" : [
	        	{ 
	        	    "id" : "c44689c6-8fce-4ce0-9ba2-5cf7b3d77a65", 
	        	    "label" : "Knowledge Management", 
	        	}
	        ],
	        "solutions" : [
	        	{ 
	        	    "id" : "cd2ff47f-01fc-4da8-bae8-d1cac61671ac", 
	        	    "label" : "Sales Portal", 
	        	}
	        ]
	    }
	},
	{ 
	    "contentQId" : "topic", 
	    "record" : {
	        "identity" : "f8e0d987-44cf-4cd6-942e-c21b40ae9991", 
	        "name" : "Sales On-Boarding", 
	        "shortName" : "Sales On-Boarding"
	    }
	}];

// Switch to system database
db = db.getSiblingDB("system_enablix");

var coll = db.getCollection("ebx_sample_content");

// clear the collection
coll.remove({});

// insert the records
contentList.forEach(function(content) {
	coll.insert(content);
});

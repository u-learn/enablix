enablix.studioApp.factory('AuditConfigService', 
	[			'RESTService', 'Notification','DataSearchService',
	 	function(RESTService,   Notification,DataSearchService) {
		var DOMAIN_TYPE = "com.enablix.core.domain.activity.ActivityAudit";
		var FILTER_METADATA = {
				"activitycat" : {
 					"field" : "activity.category",
 					"operator" : "EQ",
 					"dataType" : "STRING"
 				},
 				"auditUser" : {
 					"field" : "actor.name",
 					"operator" : "EQ",
 					"dataType" : "STRING"
 				},
 				"auditActivityType" : {
 					"field" : "activity.activityType",
 					"operator" : "EQ",
 					"dataType" : "STRING"
 				},
 				"auditEventOcc" : {
 					"field" : "activityTime",
 					"operator" : "GTE",
 					"dataType" : "DATE"
 				}
 		};
		var getAuditData= function(_filters,_pagination,_onSuccess,_onError)
		{
			DataSearchService.getSearchResult(DOMAIN_TYPE, _filters, _pagination, FILTER_METADATA, _onSuccess, _onError);
		};
		return {
				getAuditData: getAuditData
		};
		
		}
	]);
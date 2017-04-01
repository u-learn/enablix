enablix.studioApp.factory('AuditConfigService', 
	[			'RESTService', 'Notification','DataSearchService',
	 	function(RESTService,   Notification,DataSearchService) {
		var DOMAIN_TYPE = "com.enablix.core.domain.activity.ActivityAudit";
		var getAuditData= function(_filters,_pagination,filterMetaData,_onSuccess,_onError)
		{
			DataSearchService.getSearchResult(DOMAIN_TYPE, _filters, _pagination, filterMetaData, _onSuccess, _onError);
		};
		return {
				getAuditData: getAuditData
		};
		
		}
	]);
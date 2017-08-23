enablix.studioApp.factory('AuditConfigService', 
	[			'RESTService', 'Notification', 'DataSearchService',
	 	function(RESTService,   Notification,   DataSearchService) {

			var DOMAIN_TYPE = "com.enablix.core.domain.activity.ActivityAudit";

			var activityTypes = null;
			
			var getAuditData= function(_filters,_pagination,filterMetaData,_onSuccess,_onError) {
				DataSearchService.getSearchResult(DOMAIN_TYPE, _filters, _pagination, filterMetaData, _onSuccess, _onError);
			};
			
			var getActivityTypes = function() {
				return activityTypes == null ? {} : activityTypes;
			};
			
			var init = function() {
				
				if (activityTypes == null) {
					
					return RESTService.getForData('getAuditActivityTypes', null, null, function(data) {
								activityTypes = data;
			                }, function() {
			                    Notification.error({ message: "Error loading Activity data", delay: enablix.errorMsgShowTime });
			                });
					
				} else {
					return activityTypes;
				}
			}
			
			return {
				getAuditData: getAuditData,
				getActivityTypes: getActivityTypes,
				init: init
			};
		
		}
	]);
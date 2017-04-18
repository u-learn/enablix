enablix.studioApp.factory('ActivityMetricService', 
	[	        'RESTService', 'Notification', 'StateUpdateService', '$location', '$filter',
	 	function(RESTService,   Notification,   StateUpdateService,   $location,   $filter) {
		
			var getActivityMetric = function(urlKey, searchFilters, 
					_onSuccess, _onError){
				RESTService.getForData(urlKey, searchFilters, null, _onSuccess, _onError, null)
			}
			return {
				getActivityMetric: getActivityMetric,
			};
	 	}
	]);
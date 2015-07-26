enablix.studioApp.factory('RecentDataService', 
	[	'RESTService', 'Notification',
	 	function(RESTService, Notification) {
		
			var getRecentData = function(_containerQId, _contentIdentity, _onSuccess) {
				
				var params = {"containerQId" : _containerQId, 
							  "contentIdentity" : _contentIdentity};
				
				var recoUrlKey = "contentSpecificRecentData";
				
				if (isNullOrUndefined(_containerQId) && isNullOrUndefined(_contentIdentity)) {
					recoUrlKey = "generalRecentData";
					
				} else if (!isNullOrUndefined(_containerQId) && isNullOrUndefined(_contentIdentity)) {
					recoUrlKey = "containerSpecificRecentData"
				}
				
				return RESTService.getForData(recoUrlKey, params, null, _onSuccess,
					function(resp, status) {
						Notification.error({message: "Error loading recent data", delay: enablix.errorMsgShowTime});
					});
				
			};
	
			return {
				getRecentData : getRecentData
			};
		
		}
	]);
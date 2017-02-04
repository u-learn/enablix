enablix.studioApp.factory('RecentDataService', 
	[	'RESTService', 'Notification', 'DataSearchService',
	 	function(RESTService, Notification, DataSearchService) {
		
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
	
			var getRecentDataList = function(_searchFilters, _pagination, _filterMetadata, _onSuccess, _onError) {
				
				var data = {
						filters : _searchFilters,
						filterMetadata : _filterMetadata,
						pagination : _pagination
					};
					
				return RESTService.postForData("getRecentDataList", null, data, null, _onSuccess, _onError);
			};
			
			return {
				getRecentData : getRecentData,
				getRecentDataList : getRecentDataList
			};
		
		}
	]);
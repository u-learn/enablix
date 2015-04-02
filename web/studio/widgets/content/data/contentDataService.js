enablix.studioApp.factory('ContentDataService', 
	[
	 	'RESTService',
	 	function(RESTService) {
	 		
	 		var getContentData = function(_templateId, _contentQId, _parentIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
	 					"templateId": _templateId,
	 					"contentQId": _contentQId
	 			};

	 			var resourceKey = "fetchRootData";
	 			if (!angular.isUndefined(_parentIdentity) && _parentIdentity != null) {
	 				resourceKey = "fetchChildrenData";
	 				params["parentIdentity"] = _parentIdentity;
	 			}
	 			
	 			RESTService.getForData(resourceKey, params, null, _onSuccess, _onError);
	 			
	 		};

	 		var getContentRecordData = function(_templateId, _contentQId, _recordIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
	 					"templateId": _templateId,
	 					"contentQId": _contentQId,
	 					"recordIdentity": _recordIdentity
	 			};
	 			
	 			RESTService.getForData("fetchRecordData", params, null, _onSuccess, _onError);
	 			
	 		};
	 		
	 		
	 		return {
	 			getContentData: getContentData,
	 			getContentRecordData: getContentRecordData
	 		};
	 	}
	 ]);
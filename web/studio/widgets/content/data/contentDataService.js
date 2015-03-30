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

	 		var getChildRecordData = function(_templateId, _contentQId, _childRecordIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
	 					"templateId": _templateId,
	 					"contentQId": _contentQId,
	 					"recordIdentity": _childRecordIdentity
	 			};
	 			
	 			RESTService.getForData("fetchChildRecord", params, null, _onSuccess, _onError);
	 			
	 		};
	 		
	 		
	 		return {
	 			getContentData: getContentData,
	 			getChildRecordData: getChildRecordData
	 		};
	 	}
	 ]);
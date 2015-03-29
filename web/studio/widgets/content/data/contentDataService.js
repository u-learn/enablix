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
	 			if (!angular.isUndefined(_parentIdentity)) {
	 				resourceKey = "fetchChildrenData";
	 				params["parentIdentity"] = _parentIdentity;
	 			}
	 			
	 			RESTService.getForData(resourceKey, params, null, function(data) {
	 				if (_contentQId == 'product') {
	 					data = [
		 					{
		 						"identity" : "5c5b6703-6bf8-451d-a8e9-7e828153119b",
		 						"category" : "Compliance 2",
		 						"name" : "Alert Management System"
		 					},
		 					{
		 						"identity" : "5c5b6703-6bf8-451d-a8e9-7e828134444b",
		 						"category" : "Compliance 2",
		 						"name" : "KYC System"
		 					}
	 					];
	 				}
	 				_onSuccess(data);
	 			}, _onError);
	 			
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
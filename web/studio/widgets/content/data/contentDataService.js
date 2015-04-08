enablix.studioApp.factory('ContentDataService', 
	[
	 	'RESTService', 'ContentTemplateService',
	 	function(RESTService, ContentTemplateService) {
	 		
	 		var getContentData = function(_templateId, _contentQId, _parentIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
	 					"templateId": _templateId,
	 					"contentQId": _contentQId
	 			};

	 			var resourceKey = "fetchRootData";
	 			if (!angular.isUndefined(_parentIdentity) && _parentIdentity != null
	 					&& _parentIdentity != "") {
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
	 		
	 		var saveContainerData = function(_templateId, _contentQId, _parentRecordIdentity, _data, _onSuccess, _onError) {
	 			
	 			var url = "saveOrUpdateRootContainerData";
	 			
	 			var params = {
 					"templateId": _templateId,
 					"contentQId": _contentQId
	 			}
	 			
	 			if (!ContentTemplateService.isRootContainer(enablix.template, _contentQId)) {
	 				params["parentIdentity"] = _parentRecordIdentity;
	 				url = "saveOrUpdateChildContainerData";
	 			}
	 			
	 			RESTService.postForData(url, params, _data, null, _onSuccess, _onError, null);
	 		}
	 		
	 		return {
	 			getContentData: getContentData,
	 			getContentRecordData: getContentRecordData,
	 			saveContainerData: saveContainerData
	 		};
	 	}
	 ]);
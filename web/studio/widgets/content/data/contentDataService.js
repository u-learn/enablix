enablix.studioApp.factory('ContentDataService', 
	[
	 	'RESTService', 'ContentTemplateService',
	 	function(RESTService, ContentTemplateService) {
	 		
	 		var getContentData = function(_templateId, _contentQId, _parentIdentity, _onSuccess, _onError, _pagination) {
	 			
	 			var params = {
	 					"templateId": _templateId,
	 					"contentQId": _contentQId
	 			};
	 			
	 			if (!isNullOrUndefined(_pagination)) {
	 				params.page = _pagination.pageNum;
	 				params.pageSize = isNullOrUndefined(_pagination.pageSize) ? enablix.defaultPageSize : _pagination.pageSize;
	 			} else {
	 				params.page = "";
	 				params.pageSize = "";
	 			}
	 			

	 			var resourceKey = "fetchRootData";
	 			if (!isNullOrUndefined(_parentIdentity) && _parentIdentity != "") {
	 				resourceKey = "fetchChildrenData";
	 				params["parentIdentity"] = _parentIdentity;
	 			}
	 			
	 			RESTService.getForData(resourceKey, params, null, _onSuccess, _onError);
	 			
	 		};

	 		var getContentRecordData = function(_templateId, _contentQId, _recordIdentity, _accessFrom, _onSuccess, _onError) {
	 			
	 			var atChannel = _accessFrom == 'PORTAL' ? 'WEB' : null;
	 			
	 			var params = {
	 					"templateId": _templateId,
	 					"contentQId": _contentQId,
	 					"recordIdentity": _recordIdentity,
	 					"accessChannel": atChannel
	 			};
	 			
	 			RESTService.getForData("fetchRecordData", params, null, _onSuccess, _onError);
	 			
	 		};
	 		
	 		var saveContainerData = function(_templateId, _contentQId, _parentRecordIdentity, _data, _onSuccess, _onError) {
	 			
	 			var url = "insertRootContainerData";
	 			
	 			var params = {
 					"templateId": _templateId,
 					"contentQId": _contentQId
	 			};
	 			
	 			if (_data.identity && _data.identity != null) {
	 				url = "updateContainerData";
	 				
	 			} else if (!ContentTemplateService.isRootContainer(enablix.template, _contentQId)) {
	 				params["parentIdentity"] = _parentRecordIdentity;
	 				url = "insertChildContainerData";
	 			}
	 			
	 			RESTService.postForData(url, params, _data, null, _onSuccess, _onError, null);
	 		}
	 		
	 		var deleteContentData = function(_contentQId, _recordIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
	 					"templateId": enablix.template.id,
	 					"contentQId": _contentQId,
	 					"recordIdentity": _recordIdentity
		 			};
	 			
	 			RESTService.postForData("deleteContentData", params, null, null, _onSuccess, _onError, null);
	 		}
	 		
	 		var getNavigationPath = function(_containerQId, _contentIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
 					"containerQId": _containerQId,
 					"contentIdentity": _contentIdentity
	 			};

	 			RESTService.getForData("navPath", params, null, _onSuccess, _onError);
	 			
	 		};

	 		
	 		return {
	 			getContentData: getContentData,
	 			getContentRecordData: getContentRecordData,
	 			getNavigationPath: getNavigationPath,
	 			saveContainerData: saveContainerData,
	 			deleteContentData: deleteContentData
	 		};
	 	}
	 ]);
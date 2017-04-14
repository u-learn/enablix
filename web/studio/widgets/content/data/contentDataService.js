enablix.studioApp.factory('ContentDataService', 
	[
	 			'RESTService', 'ContentTemplateService', '$location', '$state', 'NavigationTracker',
	 	function(RESTService,  ContentTemplateService,    $location,   $state,   NavigationTracker) {
	 		
	 		var getContentData = function(_templateId, _contentQId, _parentIdentity, _onSuccess, _onError, _pagination) {
	 			
	 			var params = {
	 					"templateId": _templateId,
	 					"contentQId": _contentQId
	 			};
	 			
	 			if (!isNullOrUndefined(_pagination)) {
	 				params.page = _pagination.pageNum;
	 				params.pageSize = isNullOrUndefined(_pagination.pageSize) ? enablix.defaultPageSize : _pagination.pageSize;
	 				params.sortProp = _pagination.sort ? _pagination.sort.field : "";
	 				params.sortDir = _pagination.sort ? _pagination.sort.direction : "";
	 			} else {
	 				params.page = "";
	 				params.pageSize = "";
	 				params.sortProp = "";
	 				params.sortDir = "";
	 			}
	 			

	 			var resourceKey = "fetchRootData";
	 			if (!isNullOrUndefined(_parentIdentity) && _parentIdentity != "") {
	 				resourceKey = "fetchChildrenData";
	 				params["parentIdentity"] = _parentIdentity;
	 			}
	 			
	 			RESTService.getForData(resourceKey, params, null, _onSuccess, _onError);
	 			
	 		};
	 		
	 		var getRecordAndChildData = function(_contentQId, _contentIdentity, _onSuccess, _onError, _childSizeLimit, _accessFrom) {
	 			
	 			var reqParams = $location.search();
	 			setActivityTrackingParams(reqParams, _accessFrom);
	 			
	 			var params = {
	 				contentQId: _contentQId,
	 				contentIdentity: _contentIdentity
	 			}
	 			
	 			if (!isNullOrUndefined(_childSizeLimit)) {
	 				params.pageSize = _childSizeLimit;
	 			}
	 			
	 			RESTService.getForData("fetchRecordAndChildData", params, null, _onSuccess, _onError, reqParams);
	 		};

	 		var setActivityTrackingParams = function(_reqParams, _accessFrom) {
	 			
	 			var reqParams = $location.search();
	 			var previousState = NavigationTracker.getPreviousState();
	 			
	 			if (isNullOrUndefined(_reqParams.atChannel)
	 					&& !$state.includes('portal.search')) { // ignore search result listing
	 				
	 				// if navigating from search results, then track the search action as well
	 				if (previousState != null && previousState.route.name == 'portal.search') {
	 					
	 					_reqParams.atChannel = 'WEB';
	 					_reqParams.atContext = 'SEARCH_RESULT';
	 					_reqParams.atContextTerm = previousState.routeParams.searchText;
	 					
	 				} else {
	 					_reqParams.atChannel = _accessFrom == 'PORTAL' ? 'WEB' : null;
	 				}
	 			}
	 			
	 		}
	 		
	 		var getContentRecordData = function(_templateId, _contentQId, _recordIdentity, _accessFrom, _onSuccess, _onError) {
	 			
	 			var reqParams = $location.search();
	 			setActivityTrackingParams(reqParams, _accessFrom);
	 			
	 			var params = {
	 					"templateId": _templateId,
	 					"contentQId": _contentQId,
	 					"recordIdentity": _recordIdentity
	 			};
	 			
	 			return RESTService.getForData("fetchRecordData", params, null, _onSuccess, _onError, reqParams);
	 			
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
	 			
	 			var headers = {"atActivityOrigin" : "Studio"};
	 			if ($state.includes('portal')) {
	 				headers.atActivityOrigin = "Portal";
	 			}
	 			
	 			RESTService.postForData(url, params, _data, null, _onSuccess, _onError, null, headers);
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

	 		
	 		var contentChangeEventId = function(_contentQId) {
	 			return "event.content.change:" + _contentQId;
	 		};
	 		
	 		
	 		var getContentStack = function(_contentStackItems, _onSuccess, _onError) {
	 			RESTService.postForData("fetchContentStack", null, _contentStackItems, null, _onSuccess, _onError, null);
	 		};
	 		
	 		var getContentStackForRecord = function(_containerQId, _instanceIdentity, _pageData, _onSuccess, _onError) {
	 			
	 			var params = {
	 					containerQId: _containerQId,
	 					instanceIdentity: _instanceIdentity
	 			};
	 			
	 			angular.forEach(_pageData, function(val, key) {
	 				params[key] = val;
	 			});
	 			
	 			RESTService.getForData("fetchContentStackForRecord", params, null, _onSuccess, _onError);
	 		};
	 		
	 		var getContentStackItemForRecord = function(_containerQId, _instanceIdentity, _itemQId, _onSuccess, _onError) {
	 			
	 			var params = {
	 					containerQId: _containerQId,
	 					instanceIdentity: _instanceIdentity,
	 					itemQId: _itemQId
	 			};
	 			
	 			RESTService.getForData("fetchContentStackItemForRecord", params, null, _onSuccess, _onError);
	 		};
	 		
	 		return {
	 			getContentData: getContentData,
	 			getContentRecordData: getContentRecordData,
	 			getNavigationPath: getNavigationPath,
	 			saveContainerData: saveContainerData,
	 			deleteContentData: deleteContentData,
	 			contentChangeEventId: contentChangeEventId,
	 			getContentStack: getContentStack,
	 			getContentStackForRecord: getContentStackForRecord,
	 			getContentStackItemForRecord: getContentStackItemForRecord,
	 			getRecordAndChildData: getRecordAndChildData
	 		};
	 	}
	 ]);

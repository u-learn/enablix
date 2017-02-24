enablix.studioApp.factory('ContentKitService', 
	[			'RESTService', '$state', 'Notification', 'DataSearchService',
	 	function(RESTService,   $state,   Notification,   DataSearchService) {
		
			var DOMAIN_TYPE = "com.enablix.core.domain.content.kit.ContentKit";
		
			var FILTER_METADATA = {};
			var LIST_PROJECTION_FIELDS = ["identity", "name", "createdByName", "createdAt"];
			
			var saveContentKit = function(_contentKit, _onSuccess, _onError) {
				RESTService.postForData("saveContentKit", null, _contentKit, null, _onSuccess, _onError);
			};
			
			var getContentKitList = function(_filters, _pagination, _onSuccess, _onError) {
				DataSearchService.getSearchResult(DOMAIN_TYPE, _filters, _pagination, FILTER_METADATA, _onSuccess, _onError);
			};
			
			var getContentKitSummaryList = function(_filters, _pagination, _onSuccess, _onError) {
				DataSearchService.getSearchResult(DOMAIN_TYPE, _filters, _pagination, FILTER_METADATA, _onSuccess, _onError, LIST_PROJECTION_FIELDS);
			};
			
			var getContentKit = function(_kitIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
 					"contentKitIdentity": _kitIdentity
	 			};
	 			
	 			RESTService.getForData("getContentKit", params, null, _onSuccess, _onError);
	 		};
	 		
	 		var getContentKitBundle = function(_kitIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
 					"contentKitIdentity": _kitIdentity
	 			};
	 			
	 			var headers = {};
	 			if ($state.includes("portal")) {
	 				headers.atChannel = "WEB";
	 			}
	 			
	 			RESTService.getForData("getContentKitBundle", params, null, _onSuccess, _onError, headers);
	 		};
	 		
	 		var getContentKitDetail = function(_kitIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
 					"contentKitIdentity": _kitIdentity
	 			};
	 			
	 			RESTService.getForData("getContentKitDetail", params, null, _onSuccess, _onError);
	 		};
	 		
	 		var deleteContentKit = function(_contentKitIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
 					"contentKitIdentity": _contentKitIdentity
	 			};

	 			RESTService.postForData("delContentKit", params, null, null, _onSuccess, _onError, null);
	 		};
	 		
			return {
				getContentKit: getContentKit,
				getContentKitBundle: getContentKitBundle,
				getContentKitDetail: getContentKitDetail,
				saveContentKit: saveContentKit,
				getContentKitList: getContentKitList,
				getContentKitSummaryList: getContentKitSummaryList,
				deleteContentKit: deleteContentKit,
				DOMAIN_TYPE: DOMAIN_TYPE,
				LIST_PROJECTION_FIELDS: LIST_PROJECTION_FIELDS
			};
		
		}
	]);
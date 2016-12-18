enablix.studioApp.factory('ContentConnectionService', 
	[			'RESTService', 'Notification', 'DataSearchService',
	 	function(RESTService,   Notification,   DataSearchService) {
		
			var DOMAIN_TYPE = "com.enablix.core.domain.content.connection.ContentTypeConnection";
		
			var FILTER_METADATA = {
				"connCategory" : {
 					"field" : "tags.name",
 					"operator" : "EQ",
 					"dataType" : "STRING"
 				}
			};
			
			var CATEGORY_TO_BUSS_CATEGORY_MAP = {
				"businesscontent": "BUSINESS_CONTENT",
				"businessdimension": "BUSINESS_DIMENSION"
			};
			
			var CATEGORY_LABEL = {
				"businesscontent": "Content Type",
				"businessdimension": "Business Dimension"
			};
			
			var saveContentConnection = function(_contentConn, _onSuccess, _onError) {
				RESTService.postForData("saveContentConnection", null, _contentConn, null, _onSuccess, _onError);
			};
			
			var getContentConnectionList = function(_filters, _pagination, _onSuccess, _onError) {
				DataSearchService.getSearchResult(DOMAIN_TYPE, _filters, _pagination, FILTER_METADATA, _onSuccess, _onError);
			};
			
			var getContentConnection = function(_connectionIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
 					"connIdentity": _connectionIdentity
	 			};
	 			
	 			RESTService.getForData("getContentConnection", params, null, _onSuccess, _onError);
	 		};
	 		
	 		var deleteContentConnection = function(_connectionIdentity, _onSuccess, _onError) {
	 			
	 			var params = {
 					"connIdentity": _connectionIdentity
	 			};

	 			RESTService.postForData("delContentConnection", params, null, null, _onSuccess, _onError, null);
	 		};
	 		
	 		var getBusinessCategory = function(_category) {
	 			return CATEGORY_TO_BUSS_CATEGORY_MAP[_category];
	 		};
	 		
	 		var getLabelForCategory = function(_category) {
	 			return CATEGORY_LABEL[_category];
	 		};
	 		
	 		var getCategoryTag = function(_tagName) {
	 			return "category:" + _tagName;
	 		};
			
			return {
				getContentConnection: getContentConnection,
				saveContentConnection: saveContentConnection,
				getContentConnectionList: getContentConnectionList,
				deleteContentConnection: deleteContentConnection,
				getBusinessCategory: getBusinessCategory,
				getLabelForCategory: getLabelForCategory,
				getCategoryTag: getCategoryTag
			};
		
		}
	]);
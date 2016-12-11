enablix.studioApp.factory('ContentConnectionService', 
	[			'RESTService', 'Notification', 'DataSearchService',
	 	function(RESTService,   Notification,   DataSearchService) {
		
			var DOMAIN_TYPE = "com.enablix.core.domain.content.connection.ContentTypeConnection";
		
			var FILTER_METADATA = {
					
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
			
			return {
				getContentConnection: getContentConnection,
				saveContentConnection: saveContentConnection,
				getContentConnectionList: getContentConnectionList
			};
		
		}
	]);
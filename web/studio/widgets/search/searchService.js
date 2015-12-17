enablix.studioApp.factory('SearchService', 
	[	        'RESTService', 'Notification', 'StateUpdateService',
	 	function(RESTService,   Notification,   StateUpdateService) {

			var getSearchResult = function(_searchText, _onSuccess, _onError) {
				var params = {"searchText": _searchText};
				return RESTService.getForData("searchData", params, null, _onSuccess, _onError);
			};
		
			return {
				getSearchResult: getSearchResult
			};
	 	}
	]);
enablix.studioApp.factory('SearchService', 
	[	        'RESTService', 'Notification', 'StateUpdateService',
	 	function(RESTService,   Notification,   StateUpdateService) {

			var getSearchResult = function(_searchText, _pagination, _onSuccess, _onError) {
				
				var params = {
						"searchText": _searchText
				};
				
				params.pageNum = _pagination.pageNum;
 				params.pageSize = isNullOrUndefined(_pagination.pageSize) ? enablix.defaultPageSize : _pagination.pageSize;
 				
				return RESTService.getForData("searchData", params, null, _onSuccess, _onError);
			};
		
			return {
				getSearchResult: getSearchResult
			};
	 	}
	]);
enablix.studioApp.factory('DataSearchService', 
	[	        'RESTService', 'Notification', 'StateUpdateService',
	 	function(RESTService,   Notification,   StateUpdateService) {

			/**
			 * Sample inputs:
			 * 
			 * _searchFilters = {
      		 *		"activitycat" : "CONTENT"
    		 *	}
    		 *
    		 * _pagination = {
    		 * 		"pageSize" : 10,
			 *     	"pageNum" : 2,
			 *   	"sort" : {
			 *         	"field" : "activityTime",
			 *         	"direction" : "DESC"
			 *       }
    		 *  }
    		 *  
    		 *  _filterMetadata = {
    		 *  	"activitycat" : {
			 *	        "field" : "activity.category",
			 *	        "operator" : "EQ",
			 *	        "dataType" : "STRING"
			 *	    }
    		 *  }
    		 *  
			 */
		
			var getSearchResult = function(_domainType, _searchFilters, _pagination, 
									_filterMetadata, _onSuccess, _onError, _projectedFields) {
				
				var params = {
					domainType : _domainType
				};
				
				var data = {
					filters : _searchFilters,
					filterMetadata : _filterMetadata,
					pagination : _pagination,
					projectedFields: _projectedFields
				};
				
				return RESTService.postForData("dataSearchRequest", params, data, null, _onSuccess, _onError);
			};
		
			return {
				getSearchResult: getSearchResult
			};
	 	}
	]);
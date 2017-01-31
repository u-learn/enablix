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
				
				return getDataSearchResult("dataSearchRequest", params, _searchFilters, _pagination, 
						_filterMetadata, _onSuccess, _onError, _projectedFields);
			};
			
			var getDataSearchResult = function(urlKey, params, _searchFilters, _pagination, 
					_filterMetadata, _onSuccess, _onError, _projectedFields) {
				
				var data = {
						filters : _searchFilters,
						filterMetadata : _filterMetadata,
						pagination : _pagination,
						projectedFields: _projectedFields
					};
					
				return RESTService.postForData(urlKey, params, data, null, _onSuccess, _onError);
			}
			
			var getContainerDataSearchResult = function(_containerQId, _searchFilters, _pagination, 
					_filterMetadata, _onSuccess, _onError, _projectedFields) {

				var params = {
					containerQId : _containerQId
				};
				
				return getDataSearchResult("containerDataSearchRequest", params, _searchFilters, _pagination, 
					_filterMetadata, _onSuccess, _onError, _projectedFields);
				
			};
		
			return {
				getSearchResult: getSearchResult,
				getContainerDataSearchResult: getContainerDataSearchResult 
			};
	 	}
	]);
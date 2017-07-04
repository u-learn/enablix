enablix.studioApp.factory('DataSearchService',
	[			 'RESTService', 'Notification', 'StateUpdateService', 'LocationUtil', '$filter', "$q",
		function (RESTService,   Notification,   StateUpdateService,   LocationUtil,   $filter,   $q) {

			var DATE_FILTER_TYPE_GO_BACK_DAYS = "GO_BACK_DAYS";
			var FILTER_DATE_FORMAT = "dd-MMM-yy";

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

			var getSearchResult = function (_domainType, _searchFilters, _pagination,
				_filterMetadata, _onSuccess, _onError, _projectedFields) {

				var params = {
					domainType: _domainType
				};

				return getDataSearchResult("dataSearchRequest", params, _searchFilters, _pagination,
					_filterMetadata, _onSuccess, _onError, _projectedFields);
			};

			var getDataSearchResult = function (urlKey, params, _searchFilters, _pagination,
				_filterMetadata, _onSuccess, _onError, _projectedFields) {

				var data = {
					filters: _searchFilters,
					filterMetadata: _filterMetadata,
					pagination: _pagination,
					projectedFields: _projectedFields
				};

				return RESTService.postForData(urlKey, params, data, null, _onSuccess, _onError);
			}

			var getContainerDataSearchResult = function (_containerQId, _searchFilters, _pagination,
				_filterMetadata, _onSuccess, _onError, _projectedFields) {

				var params = {
					containerQId: _containerQId
				};

				return getDataSearchResult("containerDataSearchRequest", params, _searchFilters, _pagination,
					_filterMetadata, _onSuccess, _onError, _projectedFields);

			};

			var readUrlSearchFilters = function () {
				return LocationUtil.readUrlSearchFilters();
			};
			
			var updateUrlSearchFilters = function(_searchFilters, _appendPrefix) {
				LocationUtil.updateUrlSearchFilters(_searchFilters, _appendPrefix);
			}

			var BIG_REQUEST = { pageNum: "0", pageSize: 500, sort: { direction: "ASC", field: "createdAt" } };

			function promiseContainerDataSearchResult(containerQId, dataFilters, pagination, filterMetadata) {
				dataFilters = dataFilters || {};
				pagination = pagination || BIG_REQUEST;
				filterMetadata = filterMetadata || {};
				return $q(function(resolve, reject){
					function onSuccess(data) {
						resolve({ containerQId:containerQId, data:data });
					}
					function onError(errorData) {
						reject({ containerQId:containerQId, errorData:errorData });
					}
					getContainerDataSearchResult(containerQId, dataFilters, pagination, filterMetadata, onSuccess, onError);
				});
			}

			var getContainerCountByRefListItems = function(_containerQId, _searchFilters, _filterMetadata, _onSuccess, _onError) {
				
				var params = {
						containerQId: _containerQId
				};
				
				var data = {
						filters: _searchFilters,
						filterMetadata: _filterMetadata
					};

				return RESTService.postForData("getContainerCountByRefListItems", params, data, null, _onSuccess, _onError);
			}
			
			return {
				getSearchResult: getSearchResult,
				getContainerDataSearchResult: getContainerDataSearchResult,
				readUrlSearchFilters: readUrlSearchFilters,
				updateUrlSearchFilters: updateUrlSearchFilters,
				promiseContainerDataSearchResult: promiseContainerDataSearchResult,
				getContainerCountByRefListItems: getContainerCountByRefListItems 
			};
		}
	]);
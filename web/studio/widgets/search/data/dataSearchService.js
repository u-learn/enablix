enablix.studioApp.factory('DataSearchService',
	['RESTService', 'Notification', 'StateUpdateService', '$location', '$filter', "$q",
		function (RESTService, Notification, StateUpdateService, $location, $filter, $q) {

			var URL_SEARCH_FIELD_PREFIX = "sf_";
			var URL_SEARCH_FIELD_PREFIX_LEN = URL_SEARCH_FIELD_PREFIX.length;

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

				var filters = {};
				var urlParams = $location.search();

				angular.forEach(urlParams, function (value, key) {
					if (isSearchFilterParam(key)) {
						var filterKey = key.substring(URL_SEARCH_FIELD_PREFIX_LEN, key.length);
						filters[filterKey] = value;
					}
				});

				return filters;
			};
			
			var isSearchFilterParam = function (_paramKey) {
				return _paramKey.startsWith(URL_SEARCH_FIELD_PREFIX);
			}
			
			var updateUrlSearchFilters = function(_searchFilters, _appendPrefix) {
				
				var urlParams = $location.search();
				var newParams = {};
				
				angular.forEach(urlParams, function (value, key) {
					if (!isSearchFilterParam(key)) {
						newParams[key] = value;
					}
				});
				
				angular.forEach(_searchFilters, function (value, key) {
					var filterKey = _appendPrefix ? (URL_SEARCH_FIELD_PREFIX + key) : key;
					newParams[filterKey] = value;
				});
				
				$location.search(newParams);
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

			return {
				getSearchResult: getSearchResult,
				getContainerDataSearchResult: getContainerDataSearchResult,
				readUrlSearchFilters: readUrlSearchFilters,
				updateUrlSearchFilters: updateUrlSearchFilters,
				promiseContainerDataSearchResult: promiseContainerDataSearchResult
			};
		}
	]);
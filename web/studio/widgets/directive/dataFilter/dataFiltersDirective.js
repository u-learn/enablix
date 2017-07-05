enablix.studioApp.directive('ebxDataFilters', [
        '$compile', '$timeout', '$location', 'UserPreferenceService', 'Notification', 'DataSearchService',
function($compile,   $timeout,   $location,   UserPreferenceService,   Notification,   DataSearchService) {

	return {
		restrict: 'E',
		scope : {
			filters: '=',
			filterValues: '=?',
			heading: '@',
			searchLabel: '@',
			resetLabel: '@',
			searchOnPageLoad: '=',
			onSearch:'=?',
			onReset: '=?',
			prefValuesKey: "=?",
			ebLayout: "@",
			hideFiltersWithNoOptions: "=",
			persistSelection: "="
		},
		link: function(scope, element, attrs) {
			
			// the angular multi-select dropdown does not work if it is initially hidden
			// in select.js, calculateContainerWidth() function return value 0, if it is hidden.
			// As a result, the text box in the directive is only 10px and is hard to select.
			// HACK: keep the data filter panel open initially and close it after a delay. This
			// lets the angular select to render and text input has proper width.
			$timeout(function() {
				$(element).find("#dataFilterContainer").removeClass("in");
			}, 300);
			
		},
		controller: function($scope) {
			
			var filterValuesCopy = {};
			
			$scope.resetLabel = $scope.resetLabel || "Reset";
			$scope.searchLabel = $scope.searchLabel || "Search";
			$scope.filterValues = $scope.filterValues || {};
			$scope.layout = $scope.layout || "panel";
			$scope.hideFiltersWithNoOptions = $scope.hideFiltersWithNoOptions || false;
			$scope.persistSelection = $scope.persistSelection || false;

			$scope.showSaveAsDefault = !isNullOrUndefined($scope.prefValuesKey);
			
			angular.copy($scope.filterValues, filterValuesCopy);

			var readFiltersFromUrl = function() {
				
				var filterVals = DataSearchService.readUrlSearchFilters();
				
				angular.forEach($scope.filters, function(filter) {
				
					var value = filterVals[filter.id];
					
					if (value) {
						
						var filterVal = [];
						
						if (isString(value)) {
							filterVal.push({id: value});
						} else {
							angular.forEach(value, function (val) {
								filterVal.push({id: val});
							});
						}
						
						$scope.filterValues[filter.id] = filterVal;
					}
				});
			}
			
			
			function getSearchFilterValues() {
				
				var searchValues = {};
				
				var filterValuesValid = true;
				angular.forEach($scope.filters, function(filter) {
					
					var valid = true;
					if (filter.validateBeforeSubmit) {
						
						valid = filter.validateBeforeSubmit($scope.filterValues[filter.id]);
						
						if (!valid) {
							filterValuesValid = false;
						}
					}
					
					if (valid) {
						searchValues[filter.id] = filter.filterValueTransformer ? 
							filter.filterValueTransformer($scope.filterValues[filter.id]) : $scope.filterValues[filter.id];
					}
					
				});
				
				return filterValuesValid ? searchValues : null;
			}
			
			$scope.selectedFilterItems = [];
			
			var updatedSelectedFilterItems = function() {
				
				var selFilterItems = [];
				
				angular.forEach($scope.filterValues, function(value, key) {
					angular.forEach(value, function(val, indx) {
						val.filterId = key;
						val.indx = indx;
						selFilterItems.push(val);
					});
				});
				
				$scope.selectedFilterItems = selFilterItems;
			}
			
			$scope.selectedFilterItemRemoved = function($chip, $index) {
				var filterVals = $scope.filterValues[$chip.filterId];
				filterVals.splice($chip.indx, 1);
				$scope.onSearchAction();
			}
			
			$scope.clearAllFilters = function() {
				angular.forEach($scope.filterValues, function(value, key) {
					$scope.filterValues[key] = [];
				});
				$scope.onSearchAction();
			}
			
			$scope.onSearchAction = function() {
				
				if ($scope.onSearch) {
				
					var searchValues = getSearchFilterValues();
					
					if (searchValues) {
						
						$scope.onSearch(searchValues);
						
						updatedSelectedFilterItems();
						
						if ($scope.persistSelection) {
							DataSearchService.updateUrlSearchFilters(searchValues, true);
						}
					}
					
				}
			};
			
			$scope.onResetAction = function() {
				angular.copy(filterValuesCopy, $scope.filterValues);
				if ($scope.onReset){
					$scope.onReset();
				}
			}
			
			var getCurrentFilterValues = function() {
				
				var filterVals = {};
				
				angular.forEach($scope.filters, function(filter) {
					filterVals[filter.id] = $scope.filterValues[filter.id];
				});
				
				return filterVals;
			}
			
			var saveAsDefault = function(_saveFn) {
				
				if ($scope.prefValuesKey) {
					
					var filterVals = getCurrentFilterValues();
					
					_saveFn($scope.prefValuesKey, filterVals, function(data) {
					
							Notification.primary("Updated default values!");
							angular.copy(filterVals, filterValuesCopy);
							
						}, function (errorData) {
							Notification.error({message: "Unable to update default values", delay: enablix.errorMsgShowTime});
						});
				}
			}
			
			$scope.saveAsUserDefault = function() {
				saveAsDefault(UserPreferenceService.saveAsUserPref);
			}
			
			$scope.saveAsSystemDefault = function() {
				saveAsDefault(UserPreferenceService.saveAsSystemPref);
			}
			
			var fetchFilterDefaultValueFromUserPref = function(_filter, _defaultPrefFVs) {
				
				if (!isNullOrUndefined(_defaultPrefFVs)) {
					return _defaultPrefFVs.config[_filter.id];
				}
				
				return null;
			}
			
			var initSearchFilters = function() {
				
				var defaultPrefFVs = $scope.prefValuesKey ? 
						UserPreferenceService.getPrefByKey($scope.prefValuesKey) : null;
				
				var searchFilters = {};
				angular.forEach($scope.filters, function(filter) {
					
					if (!isNullOrUndefined($scope.filterValues)) {
						
						// check user preferences first
						var defaultFV = fetchFilterDefaultValueFromUserPref(filter, defaultPrefFVs);
						
						if (isNullOrUndefined(defaultFV) && filter.defaultValue) {
							// check report definition if not present in user pref
							defaultFV = filter.defaultValue();
						}
		
						if (!isNullOrUndefined(defaultFV)) {
							
							$scope.filterValues[filter.id] = defaultFV;
							
							searchFilters[filter.id] = filter.filterValueTransformer ? 
									filter.filterValueTransformer(defaultFV) : defaultFV;
						}
					}
				});
			}
			
			initSearchFilters();
			readFiltersFromUrl();
			
			if ($scope.searchOnPageLoad) {
				$scope.onSearchAction();
			}
			
			$scope.toggleFilterContainer = function($event) {
				var elem = $event.currentTarget;
				if ($event.target.nodeName != 'A' && $event.target.nodeName != 'SPAN') {
					$(elem).toggleClass('closed');
					$(elem).next().slideToggle('fast');
				}
			};
			
			if ($scope.hideFiltersWithNoOptions) {
				$scope.$on("msf:opt-init-complete", function(event, _initData) {
					
					if (_initData.options.length == 0) {
						
						for (var i = 0; i < $scope.filters.length; i++) {
							if ($scope.filters[i] === _initData.filterDef) {
								$scope.filters.splice(i, 1);
								break;
							}
						}
						
					}
				});
			}
			
		},
		templateUrl: function(elem, attr) {
			var layout = attr.ebLayout || "panel";
			return "widgets/directive/dataFilter/dataFilters-" + layout + ".html"
		}
	};
}]);
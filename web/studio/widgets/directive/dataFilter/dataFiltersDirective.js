enablix.studioApp.directive('ebxDataFilters', [
        '$compile', '$timeout', 'UserPreferenceService', 'Notification',
function($compile,   $timeout,   UserPreferenceService,   Notification) {

	return {
		restrict: 'E',
		scope : {
			filters: '=',
			filterValues: '=',
			heading: '@',
			searchLabel: '@',
			resetLabel: '@',
			onSearch:'=?',
			onReset: '=?',
			prefValuesKey: "=?"
		},
		link: function(scope, element, attrs) {
			
			var filterValuesCopy = {};
			
			scope.resetLabel = scope.resetLabel || "Reset";
			scope.searchLabel = scope.searchLabel || "Search";
			scope.filterValues = scope.filterValues || {};

			scope.showSaveAsDefault = !isNullOrUndefined(scope.prefValuesKey);
			
			angular.copy(scope.filterValues, filterValuesCopy);
			
			scope.onSearchAction = function() {
				
				if (scope.onSearch) {
				
					var searchValues = {};
					
					var filterValuesValid = true;
					angular.forEach(scope.filters, function(filter) {
						
						var valid = true;
						if (filter.validateBeforeSubmit) {
							
							valid = filter.validateBeforeSubmit(scope.filterValues[filter.id]);
							
							if (!valid) {
								filterValuesValid = false;
							}
						}
						
						if (valid) {
							searchValues[filter.id] = filter.filterValueTransformer ? 
								filter.filterValueTransformer(scope.filterValues[filter.id]) : scope.filterValues[filter.id];
						}
						
					});
					
					if (filterValuesValid) {
						scope.onSearch(searchValues);
					}
					
				}
			};
			
			scope.onResetAction = function() {
				angular.copy(filterValuesCopy, scope.filterValues);
				if (scope.onReset){
					scope.onReset();
				}
			}
			
			var getCurrentFilterValues = function() {
				
				var filterVals = {};
				
				angular.forEach(scope.filters, function(filter) {
					filterVals[filter.id] = scope.filterValues[filter.id];
				});
				
				return filterVals;
			}
			
			var saveAsDefault = function(_saveFn) {
				
				if (scope.prefValuesKey) {
					
					_saveFn(scope.prefValuesKey, getCurrentFilterValues(), function(data) {
							Notification.primary("Updated default values!");
						}, function (errorData) {
							Notification.error({message: "Unable to update default values", delay: enablix.errorMsgShowTime});
						});
				}
			}
			
			scope.saveAsUserDefault = function() {
				saveAsDefault(UserPreferenceService.saveAsUserPref);
			}
			
			scope.saveAsSystemDefault = function() {
				saveAsDefault(UserPreferenceService.saveAsSystemPref);
			}
			
			// the angular multi-select dropdown does not work if it is initially hidden
			// in select.js, calculateContainerWidth() function return value 0, if it is hidden.
			// As a result, the text box in the directive is only 10px and is hard to select.
			// HACK: keep the data filter panel open initially and close it after a delay. This
			// lets the angular select to render and text input has proper width.
			$timeout(function() {
				$(element).find("#dataFilterContainer").removeClass("in");
			}, 300);
			
		},
		templateUrl: "widgets/directive/dataFilter/dataFilters.html"
	};
}]);
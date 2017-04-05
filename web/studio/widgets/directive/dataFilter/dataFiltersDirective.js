enablix.studioApp.directive('ebxDataFilters', [
        '$compile',
function($compile) {

	return {
		restrict: 'E',
		scope : {
			filters: '=',
			filterValues: '=',
			heading: '@',
			searchLabel: '@',
			resetLabel: '@',
			onSearch:'=?',
			onReset: '=?'
		},
		link: function(scope, element, attrs) {
			
			var filterValuesCopy = {};
			
			scope.resetLabel = scope.resetLabel || "Reset";
			scope.searchLabel = scope.searchLabel || "Search";
			scope.filterValues = scope.filterValues || {};

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
			
		},
		templateUrl: "widgets/directive/dataFilter/dataFilters.html"
	};
}]);
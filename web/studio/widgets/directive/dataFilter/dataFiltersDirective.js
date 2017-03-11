enablix.studioApp.directive('ebxDataFilters', [
        '$compile',
function($compile) {

	return {
		restrict: 'E',
		scope : {
			filters: '=',
			filterValues: '=',
			heading: '@',
			onSearch:'=?',
			onReset: '=?'
		},
		link: function(scope, element, attrs) {
			
			var filterValuesCopy = {};
			
			scope.filterValues = scope.filterValues || {};

			angular.copy(scope.filterValues, filterValuesCopy);
			
			scope.onSearchAction = function() {
				
				if (scope.onSearch) {
				
					var searchValues = {};
					
					angular.forEach(scope.filters, function(filter) {
						searchValues[filter.id] = filter.filterValueTransformer ? 
								filter.filterValueTransformer(scope.filterValues[filter.id]) : scope.filterValues[filter.id]; 
					});
					
					scope.onSearch(searchValues);
				}
			};
			
			scope.onResetAction = function() {
				angular.copy(filterValuesCopy, scope.filterValues);
			}
			
		},
		templateUrl: "widgets/directive/dataFilter/dataFilters.html"
	};
}]);
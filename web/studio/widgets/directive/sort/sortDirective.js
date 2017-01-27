enablix.studioApp.directive('ebSort', [
        'StateUpdateService',
function(StateUpdateService) {

	return {
		restrict: 'E',
		scope : {
			sortProperty : "=",
			tableSortInfo : "=",
			sortDataFn : "="
		},
		link: function(scope, element, attrs) {
			
			scope.sortData = function(sortProperty, sortDir) {
				if (scope.sortDataFn) {
					scope.sortDataFn(sortProperty, sortDir);
				}
		    };
			
		},

		templateUrl: "widgets/directive/sort/sort.html"
	};
}]);
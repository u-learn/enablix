enablix.studioApp.directive('ebTable', [
        'StateUpdateService',
function(StateUpdateService) {

	return {
		restrict: 'E',
		scope : {
			tableData : "=",
			tableHeaders : "=",
			rowClickFn : "="
		},
		link: function(scope, element, attrs) {
			scope.navToRowDetail = function(elementIdentity) {
				if (scope.rowClickFn) {
					scope.rowClickFn(elementIdentity);
				}
			};
		},

		templateUrl: "widgets/directive/table/defaultTable.html"
	};
}]);
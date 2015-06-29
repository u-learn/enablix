enablix.studioApp.directive('ebTable', [
        'StateUpdateService',
function(StateUpdateService) {

	return {
		restrict: 'E',
		scope : {
			tableData : "=",
			tableHeaders : "=",
			rowClickFn : "=",
			recordEditFn : "=",
			recordDeleteFn : "="
		},
		link: function(scope, element, attrs) {
			scope.navToRowDetail = function(elementIdentity) {
				if (scope.rowClickFn) {
					scope.rowClickFn(elementIdentity);
				}
			};
			
			scope.navToRecordEdit = function(elementIdentity) {
				if (scope.recordEditFn) {
					scope.recordEditFn(elementIdentity);
				}
			};
			
			scope.deleteTheRecord = function(elementIdentity) {
				if (scope.recordDeleteFn) {
					scope.recordDeleteFn(elementIdentity);
				}
			};
		},

		templateUrl: "widgets/directive/table/defaultTable.html"
	};
}]);
enablix.studioApp.directive('ebTable', function() {

	return {
		restrict: 'E',
		scope : {
			tableData : "="
		},
		link: function(scope, element, attrs) {
			scope.tableHeaders = eval("(" + attrs.tableHeaders + ")"); 
		},

		templateUrl: "widgets/directive/table/defaultTable.html"
	}
});
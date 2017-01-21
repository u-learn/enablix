enablix.studioApp.directive('ebxPlayExecutionDef', function() {

	return {
		restrict: 'E',
		scope : {
			executionDef : "=",
			focusItems : "=",
			contentGroupsDef : "=",
			userGroupsDef: "="
		},

		link: function(scope, element, attrs) {
		},

		controller: 'PlayExecutionDefCtrl',
		templateUrl: "widgets/directive/play/execution/playExecutionDef.html"
	}
});
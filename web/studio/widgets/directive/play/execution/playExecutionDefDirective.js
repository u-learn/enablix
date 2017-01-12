enablix.studioApp.directive('ebxPlayExecutionDef', function() {

	return {
		restrict: 'E',
		scope : {
			executionDef : "="
		},

		link: function(scope, element, attrs) {
		},

		controller: 'PlayExecutionDefCtrl',
		templateUrl: "widgets/directive/play/execution/playExecutionDef.html"
	}
});
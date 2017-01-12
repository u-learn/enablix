enablix.studioApp.directive('ebxPlayScope', function() {

	return {
		restrict: 'E',
		scope : {
			scopeDef : "="
		},

		link: function(scope, element, attrs) {
			scope.isCollapsed = false;
		},

		templateUrl: "widgets/directive/play/scope/playScope.html"
	}
});
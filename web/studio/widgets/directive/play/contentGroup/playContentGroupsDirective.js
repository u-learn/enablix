enablix.studioApp.directive('ebxPlayContentGroups', function() {

	return {
		restrict: 'E',
		scope : {
			contentGroupsDef : "=",
			focusItems : "="
		},

		link: function(scope, element, attrs) {
		},

		controller: 'PlayContentGroupsCtrl',
		templateUrl: "widgets/directive/play/contentGroup/playContentGroups.html"
	}
});
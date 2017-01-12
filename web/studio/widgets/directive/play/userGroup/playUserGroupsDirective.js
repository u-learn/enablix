enablix.studioApp.directive('ebxPlayUserGroups', function() {

	return {
		restrict: 'E',
		scope : {
			userGroupsDef : "="
		},

		link: function(scope, element, attrs) {
		},

		controller: 'PlayUserGroupsCtrl',
		templateUrl: "widgets/directive/play/userGroup/playUserGroups.html"
	}
});
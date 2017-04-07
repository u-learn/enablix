enablix.studioApp.directive('ebxRecordContextMenu',
			['RESTService', 'Notification', '$state', 'StateUpdateService',
	function (RESTService,   Notification,   $state,   StateUpdateService) {
	return {
		restrict: 'E',
		scope : {
			contentRecord : "=",
			contentQId : "="
		},
		link: function(scope, element, attrs) {
			
		},
		templateUrl: "widgets/directive/contextmenu/recordContextMenu.html"
	};
}]);

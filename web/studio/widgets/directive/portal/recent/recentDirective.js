enablix.studioApp.directive('ebPortalRecent', [
	function() {

		return {
			restrict : 'E',
			scope : {
				containerQId : "=",
				contentIdentity : "="
			},
			controller : 'PortalRecentCtrl',
			templateUrl : "widgets/directive/portal/recent/recent.html"
		};
	} ]);
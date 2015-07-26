enablix.studioApp.directive('ebPortalQuickLinks', [
	function() {

		return {
			restrict : 'E',
			scope : {
				containerQId : "=",
				contentIdentity : "="
			},
			controller : 'PortalQuickLinksCtrl',
			templateUrl : "widgets/directive/portal/quicklinks/quickLinks.html"
		};
	} ]);
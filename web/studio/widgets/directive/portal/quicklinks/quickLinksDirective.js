enablix.studioApp.directive('ebPortalQuickLinks', [
	function() {

		return {
			restrict : 'E',
			scope : {
				containerQId : "=",
				contentIdentity : "=",
				sectionWidth: "@" // each section width in % and in multiple of 5 e.g. section-width="25"
			},
			controller : 'PortalQuickLinksCtrl',
			templateUrl : "widgets/directive/portal/quicklinks/quickLinks.html"
		};
	} ]);
enablix.studioApp.directive('ebPortalReco', [
	function() {

		return {
			restrict : 'E',
			scope : {
				containerQId : "=",
				contentIdentity : "="
			},
			controller : 'PortalRecoCtrl',
			templateUrl : "widgets/directive/portal/reco/reco.html"
		};
	} ]);
enablix.studioApp.directive('ebPortalPeers', [
	function() {

		return {
			restrict : 'E',
			scope : {
				containerQId : "=",
				contentIdentity : "="
			},
			controller : 'PortalPeersCtrl',
			templateUrl : "widgets/directive/portal/peers/peers.html"
		};
	} ]);
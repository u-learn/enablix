enablix.studioApp.directive('ebPortalLeftNav', [
	function() {

		return {
			restrict : 'E',
			scope : {
			},
			controller : 'PortalLeftNavCtrl',
			templateUrl : "widgets/directive/portal/leftnav/nav.html"
		};
	} ]);
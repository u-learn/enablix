enablix.studioApp.directive('ebNavLink', [
	function() {

		return {
			restrict : 'E',
			scope : {
				navLink : "=",
				hideHierarchy : "="
			},
			controller : 'PortalNavLinkCtrl',
			templateUrl : "widgets/directive/portal/link/navLink.html"
		};
	} ]);
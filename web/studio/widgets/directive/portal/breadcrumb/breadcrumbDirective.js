enablix.studioApp.directive('ebPortalBreadcrumb', [
	function() {

		return {
			restrict : 'E',
			scope : {
			},
			controller : 'PortalBreadcrumbCtrl',
			templateUrl : "widgets/directive/portal/breadcrumb/breadcrumb.html"
		};
	} ]);
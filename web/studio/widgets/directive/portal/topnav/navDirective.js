enablix.studioApp.directive('ebPortalTopNav', [
	function() {

		return {
			restrict : 'E',
			scope : {
				navSelectFn : '=' 
			},
			controller : 'PortalTopNavCtrl',
			templateUrl : "widgets/directive/portal/topnav/nav.html"
		};
	} ]);
enablix.studioApp.factory('NavigationTracker', 
	[
	 			'$rootScope',
	 	function($rootScope) {
	 		
	 		var previousState = null;
	 		
			$rootScope.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
				previousState = {route: from, routeParams: fromParams};
			});
			
	 		var getPreviousState = function() {
	 			return previousState;
	 		};
	 		
	 		return {
	 			getPreviousState: getPreviousState
	 		};
	 	}
	 ]);
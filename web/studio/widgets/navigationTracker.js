enablix.studioApp.factory('NavigationTracker', 
	[
	 			'$rootScope',
	 	function($rootScope) {
	 		
	 		var navHistory = [];
	 		var recordingOn = true;
	 		
			$rootScope.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
				
				if (recordingOn) {
					navHistory.push({route: from, routeParams: fromParams});
				} else {
					recordingOn = true;
				}
			});
			
	 		var getPreviousState = function(removeFromHistory) {
	 			
	 			if (removeFromHistory) {
	 				return navHistory.pop();
	 			} else {
	 				var navLength = navHistory.length;
	 				return navLength > 0 ? navHistory[navHistory.length - 1] : null;
	 			}
	 			
	 		};
	 		
	 		var stopRecording = function() {
	 			recordingOn = false;
	 		};
	 		
	 		return {
	 			getPreviousState: getPreviousState,
	 			stopRecording: stopRecording
	 		};
	 	}
	 ]);
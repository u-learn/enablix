enablix.studioApp.controller('MainPortalCtrl', 
		   ['$scope', '$state', 'StateUpdateService', 'StudioSetupService', 
    function($scope,   $state,   StateUpdateService,   StudioSetupService) {
		if ($state.current.name === 'portal') {
			StateUpdateService.goToPortalHome();
		}
	}                                          
]);
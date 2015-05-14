enablix.studioApp.controller('MainPortalCtrl', 
		   ['$scope', 'StateUpdateService', 'StudioSetupService', 
    function($scope,   StateUpdateService,   StudioSetupService) {
		StateUpdateService.goToPortalHome();
	}                                          
]);
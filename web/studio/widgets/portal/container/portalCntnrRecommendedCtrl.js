enablix.studioApp.controller('PortalCntnrRecommendedCtrl', 
		   ['$scope', '$rootScope', '$stateParams', 'StudioSetupService', 
    function($scope,   $rootScope,   $stateParams,   StudioSetupService) {
		$scope.containerQId = $stateParams.containerQId;
		$scope.contentIdentity = $stateParams.elementIdentity;
	}                                          
]);
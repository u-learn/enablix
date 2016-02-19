enablix.studioApp.controller('PortalCntnrRecommendedCtrl', 
		   ['$scope', '$rootScope', '$state', '$stateParams', 'ContentTemplateService', 'StudioSetupService', 
    function($scope,   $rootScope,   $state,   $stateParams,   ContentTemplateService,   StudioSetupService) {
		
		$scope.$state = $state;
			   
		$scope.containerQId = $stateParams.containerQId;
		$scope.contentIdentity = $stateParams.elementIdentity;
		
		var container = ContentTemplateService.getContainerDefinition(enablix.template, $scope.containerQId);
		$scope.containerLabel = container.label;
	}                                          
]);
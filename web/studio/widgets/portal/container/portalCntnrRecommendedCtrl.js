enablix.studioApp.controller('PortalCntnrRecommendedCtrl', 
		   ['$scope', '$rootScope', '$stateParams', 'ContentTemplateService', 'StudioSetupService', 
    function($scope,   $rootScope,   $stateParams,   ContentTemplateService,   StudioSetupService) {
		$scope.containerQId = $stateParams.containerQId;
		$scope.contentIdentity = $stateParams.elementIdentity;
		
		var container = ContentTemplateService.getContainerDefinition(enablix.template, $scope.containerQId);
		$scope.containerLabel = container.label;
	}                                          
]);
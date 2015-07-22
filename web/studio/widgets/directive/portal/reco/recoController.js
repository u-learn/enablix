enablix.studioApp.controller('PortalRecoCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'RecommendationService', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   RecommendationService,   ContentTemplateService) {
		
		RecommendationService.getRecommendations($scope.containerQId, $scope.contentIdentity, 
			function(recommendations) {
				console.log(recommendations);
				$scope.recoList = recommendations;
			});
		
	}]);

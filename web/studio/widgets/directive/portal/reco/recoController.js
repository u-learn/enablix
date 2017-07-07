enablix.studioApp.controller('PortalRecoCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'RecommendationService', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   RecommendationService,   ContentTemplateService) {
		
		RecommendationService.getRecommendations($scope.containerQId, $scope.contentIdentity, 
			function(recommendations) {
				$scope.recoList = recommendations;
			});
		
		$scope.recoShowLimit = 5;
		$scope.clearShowLimit = function() {
			$scope.recoShowLimit = $scope.recoList.length;
		}
		
	}]);

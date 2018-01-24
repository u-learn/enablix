enablix.studioApp.controller('PortalRecoCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'RecommendationService', 'ContentTemplateService', 'UserPreferenceService',
    function ($scope,   StateUpdateService,   $stateParams,   RecommendationService,   ContentTemplateService,   UserPreferenceService) {
		
		RecommendationService.getRecommendations($scope.containerQId, $scope.contentIdentity, 
			function(recommendations) {
				$scope.recoList = recommendations;
			});
		
		$scope.recoShowLimit = 5;
		$scope.clearShowLimit = function() {
			$scope.recoShowLimit = $scope.recoList.length;
		}
		
		$scope.hideHierarchy = false;
		var recoPref = UserPreferenceService.getPrefByKey("portal.recommendation");
		if (!isNullOrUndefined(recoPref)) {
			$scope.hideHierarchy = recoPref.config["hideHierarchyInNavLinks"];
		}
	}]);

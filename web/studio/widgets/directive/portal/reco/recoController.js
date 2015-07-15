enablix.studioApp.controller('PortalRecoCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'RecommendationService', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   RecommendationService,   ContentTemplateService) {
		
		RecommendationService.getRecommendations($scope.containerQId, $scope.contentIdentity, 
			function(recommendations) {
				
				$scope.recoList = [];
			
				angular.forEach(recommendations, function(reco) {
					
					var firstLevelContainerQId = reco.qualifiedId;
					var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, firstLevelContainerQId);
					
					var updatedReco = [];
					
					if (!isNullOrUndefined(containerDef)) {
						updatedReco.push({
							label : containerDef.label,
							qualifiedId : firstLevelContainerQId,
							next : reco
						});
					}
					
					var navContentPointer = reco;
					while (!isNullOrUndefined(navContentPointer)) {
						updatedReco.push(navContentPointer);
						navContentPointer = navContentPointer.next;
					}
					
					$scope.recoList.push(updatedReco);
				})
			});
	}]);

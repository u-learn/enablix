enablix.studioApp.controller('PortalRecoCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'RecommendationService', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   RecommendationService,   ContentTemplateService) {
		
		RecommendationService.getRecommendations($scope.containerQId, $scope.contentIdentity, 
			function(recommendations) {
				
				console.log(recommendations);
			
				$scope.recoList = [];
			
				angular.forEach(recommendations, function(reco) {
					
					var firstLevelContainerQId = reco.qualifiedId;
					var containerDef = ContentTemplateService.getContainerDefinition(
											enablix.template, firstLevelContainerQId);
					
					var updatedReco = [];
					
					if (!isNullOrUndefined(containerDef)) {
						var first = {
								label : containerDef.label,
								qualifiedId : firstLevelContainerQId,
								next : reco
							};
						
						updatedReco.push(first);
					}

					var previousItem = null;
					var navContentPointer = reco;
					while (!isNullOrUndefined(navContentPointer)) {
						updatedReco.push(navContentPointer);
						navContentPointer.previous = previousItem;
						
						// move pointer to next
						previousItem = navContentPointer;
						navContentPointer = navContentPointer.next;
					}
					
					$scope.recoList.push(updatedReco);
				})
			});
		
		$scope.navToContent = function(navItem) {
			
			var _containerQId = navItem.qualifiedId;
			var _contentIdentity = navItem.identity;
			
			if (ContentTemplateService.isRootContainer(enablix.template, _containerQId)) {
				
				StateUpdateService.goToPortalContainerBody(
						_containerQId, _contentIdentity, 'single', _containerQId);
				
			} else {
				
				// find the root container
				var rootNavItem = navItem;
				while (!isNullOrUndefined(rootNavItem.previous)) {
					rootNavItem = rootNavItem.previous;
				}
				
				StateUpdateService.goToPortalSubItem(
						rootNavItem.qualifiedId, rootNavItem.identity, _containerQId, _contentIdentity);
			}
		}
		
	}]);

enablix.studioApp.controller('ManageRecoController', 
			['$scope', '$stateParams', 'RecommendationService', 'StateUpdateService', 'StudioSetupService', 'Notification', '$modalInstance', 'containerQId', 'contentInstanceIdentity',
	function( $scope,   $stateParams,   RecommendationService,   StateUpdateService,   StudioSetupService,   Notification,   $modalInstance,   containerQId,   contentInstanceIdentity) {
		
		$scope.close = function() {
			$modalInstance.close();
		}
		
		$scope.addRecommendation = function(assoc) {
			RecommendationService.addRecommendation(containerQId, contentInstanceIdentity,
				function(data) {
					Notification.primary("Added successfully!");
					$scope.close();
				}, 
				function(data) {
					Notification.error({message: "Error adding Recommendation", delay: enablix.errorMsgShowTime});
					$scope.close();
				});
		}
		
	}
]);
enablix.studioApp.controller('RecommendationConfigController', 
			['$scope', '$state', '$stateParams', 'RecommendationService', 'StateUpdateService', 'Notification',
	function( $scope,   $state,   $stateParams,   RecommendationService,   StateUpdateService,   Notification) {
	
		$scope.recoList = {};
		
		RecommendationService.fetchRecommendationList(function(recommendations) {
			if (!isNullOrUndefined(recommendations)) {
				$scope.recoList = recommendations;
			}
		});
		
		$scope.deleteRecommendation = function(indx) {

			if ($scope.recoList[indx] && $scope.recoList[indx].recommendation.identity) {
			
				RecommendationService.deleteRecommendation($scope.recoList[indx].recommendation.identity, 
					function(data) {
						$scope.recoList.splice(indx, 1);
					}, function(data) {
						Notification.error({message: "Error deleting Recommendation", delay: enablix.errorMsgShowTime});
					});
				
			}
		};
		
	}
]);			

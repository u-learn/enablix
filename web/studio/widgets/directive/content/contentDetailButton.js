enablix.studioApp.directive('ebxContentDetailBtn', [
                 'StateUpdateService',
		function (StateUpdateService) {

			return {
				restrict : 'E',
				scope : {
					contentQId: "=",
					contentIdentity: "="
				},
				controller : function($scope) {
					
					$scope.navToContentDetail = function() {
						StateUpdateService.goToPortalContainerDetail($scope.contentQId, $scope.contentIdentity);
					}
						
				},
				template : "<span ng-click='navToContentDetail()'><a class='fa fa-ellipsis-h'></a></span>"
			};
		} ]);
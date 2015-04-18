enablix.studioApp.controller('refdataIndexCtrl', 
			['$scope', '$state', '$stateParams', 'RefdataIndexService', 'StateUpdateService', 
    function( $scope,   $state,   $stateParams,   RefdataIndexService,   StateUpdateService) {
	
		$scope.refdataIndexData = [];
		$scope.selectedIndexId = "";
		
		RefdataIndexService.getIndexData(enablix.templateId, 
				function(data) {
					$scope.refdataIndexData = data;
				}, 
				function(response) {
					alert("Error retrieving setup index data");
				});
		
		$scope.postDataSave = function(data) {
			StateUpdateService.goToRefdataList(RefdataIndexService.getCurrentContainerQId());
		};
		
		$scope.goToAddContent = function(containerQId) {
			StateUpdateService.goToRefdataAdd(containerQId);
		};
		
		$scope.navToRefdataList = function(containerQId) {
			$scope.selectedIndexId = containerQId; 
			RefdataIndexService.setCurrentContainerQId(containerQId);
			StateUpdateService.goToRefdataList(containerQId);
		}
		
		$scope.goToContentDetail = function(elementIdentity) {
			// do nothing
		};
		
	}]);
enablix.studioApp.controller('refdataIndexCtrl', 
			['$scope', '$state', '$stateParams', 'RefdataIndexService', 'StateUpdateService', 
    function( $scope,   $state,   $stateParams,   RefdataIndexService,   StateUpdateService) {
	
		$scope.refdataIndexData = [];
		$scope.selectedIndexId = $stateParams.containerQId;
		$scope.$stateParams = $stateParams;
		
		$scope.refdataIndexData = RefdataIndexService.getIndexData();
		
		$scope.postDataSave = function(data) {
			StateUpdateService.goToRefdataList(RefdataIndexService.getCurrentContainerQId());
		};
		
		$scope.postDataUpdate = function(data) {
			StateUpdateService.goToRefdataDetail(RefdataIndexService.getCurrentContainerQId(), data.identity);
		}
		
		$scope.postDataDelete = function(parentNode, deletedChildIdentity) {
			StateUpdateService.goToRefdataList(RefdataIndexService.getCurrentContainerQId());
		}
		
		$scope.goToAddContent = function(containerQId) {
			StateUpdateService.goToRefdataAdd(containerQId);
		};
		
		$scope.navToRefdataList = function(containerQId) {
			$scope.selectedIndexId = containerQId; 
			RefdataIndexService.setCurrentContainerQId(containerQId);
			StateUpdateService.goToRefdataList(containerQId);
		}
		
		$scope.goToContentDetail = function(containerQId, elementIdentity) {
			StateUpdateService.goToRefdataDetail(containerQId, elementIdentity);
		};
		
		$scope.goToDetailEdit = function(containerQId, elementIdentity) {
			StateUpdateService.goToRefdataEdit(containerQId, elementIdentity);
		}
		
		$scope.addCancelled = function() {
			$scope.navToRefdataList(RefdataIndexService.getCurrentContainerQId());
		}
		
		$scope.updateCancelled = function(data) {
			StateUpdateService.goToRefdataDetail(RefdataIndexService.getCurrentContainerQId(), data.identity);
		}
		
		if (isNullOrUndefined($scope.selectedIndexId) && 
				$scope.refdataIndexData && 
				$scope.refdataIndexData.length > 0) {
			var firstRefdata = $scope.refdataIndexData[0];
			$scope.navToRefdataList(firstRefdata.id);
		}
		
	}]);
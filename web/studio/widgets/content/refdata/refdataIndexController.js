enablix.studioApp.controller('refdataIndexCtrl', 
			['$scope', '$state', '$stateParams', 'RefdataIndexService', 'StateUpdateService', 
    function( $scope,   $state,   $stateParams,   RefdataIndexService,   StateUpdateService) {
	
		$scope.refdataIndexData = [];
		$scope.selectedIndexId = "";
		
		$scope.refdataIndexData = RefdataIndexService.getIndexData(); 
		
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
		
		$scope.goToContentDetail = function(containerQId, elementIdentity) {
			StateUpdateService.goToRefdataDetail(containerQId, elementIdentity);
		};
		
		$scope.goToDetailEdit = function(containerQId, elementIdentity) {
			StateUpdateService.goToRefdataEdit(containerQId, elementIdentity);
		}
		
	}]);
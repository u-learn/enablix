enablix.studioApp.controller('SearchResultItemCtrl',
			['$scope', 'StateUpdateService', '$stateParams',
    function ($scope,   StateUpdateService,   $stateParams) {
		
			var navItemPointer = $scope.resultItem;
			while (navItemPointer != null) {
				$scope.resultItemQId = navItemPointer.qualifiedId;
				$scope.resultItemIdentity = navItemPointer.identity;
				$scope.containerLabel = navItemPointer.containerLabel;
				navItemPointer = navItemPointer.next;
			} 
			
	}]);

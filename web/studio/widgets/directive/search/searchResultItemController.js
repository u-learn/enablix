enablix.studioApp.controller('SearchResultItemCtrl',
			['$scope', 'StateUpdateService', '$stateParams',
    function ($scope,   StateUpdateService,   $stateParams) {
		
			var navItemPointer = $scope.resultItem;
			while (navItemPointer != null) {
				$scope.resultItemQId = navItemPointer.qualifiedId;
				$scope.resultItemIdentity = navItemPointer.identity;
				navItemPointer = navItemPointer.next;
			} 
			
	}]);

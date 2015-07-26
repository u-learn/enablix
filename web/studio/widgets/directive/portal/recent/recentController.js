enablix.studioApp.controller('PortalRecentCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'RecentDataService', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   RecentDataService,   ContentTemplateService) {
		
		RecentDataService.getRecentData($scope.containerQId, $scope.contentIdentity, 
			function(recentDataList) {
				console.log(recentDataList);
				$scope.recentList = recentDataList;
			});
		
	}]);

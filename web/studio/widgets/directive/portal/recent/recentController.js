enablix.studioApp.controller('PortalRecentCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'RecentDataService', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   RecentDataService,   ContentTemplateService) {
		
		RecentDataService.getRecentData($scope.containerQId, $scope.contentIdentity, 
			function(recentDataList) {
				$scope.recentList = recentDataList;
			});
		
		$scope.showRecentDataList = function() {
			StateUpdateService.goToRecentUpdateList({sf_lastXDays : 30});
		}
		
	}]);

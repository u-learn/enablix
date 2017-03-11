enablix.studioApp.controller('ReportsMainController', 
			['$scope', '$state', '$stateParams', '$rootScope', 'ReportService', 'Notification', 'StateUpdateService',
	function( $scope,   $state,   $stateParams,   $rootScope,   ReportService,   Notification,   StateUpdateService) {
		
		$scope.breadcrumbList = [
			{ label: "Reports" }
		];
		
		$scope.$state = $state;
		$scope.$stateParams = $state.params;
		
		$rootScope.$on('$stateChangeSuccess', 
			function(event, toState, toParams, fromState, fromParams) {
				if ($state.includes("reports")) {
					$scope.$stateParams = toParams;
				}
			});
				
		$scope.reportDefinitions = ReportService.getReportDefinitions();
		
		$scope.navToReport = function(reportDef) {
			StateUpdateService.goToReportDetail(reportDef.id);
		};
		
	}
]);

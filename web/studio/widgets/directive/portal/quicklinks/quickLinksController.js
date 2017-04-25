enablix.studioApp.controller('PortalQuickLinksCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'QuickLinksService', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   QuickLinksService,   ContentTemplateService) {

		$scope.sectionWidth = $scope.sectionWidth || "25";
				
		QuickLinksService.getUserQuickLinks( 
			function(quickLinks) {
				$scope.quickLinks = quickLinks;
			});
		
	}]);

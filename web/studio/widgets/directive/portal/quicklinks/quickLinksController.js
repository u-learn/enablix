enablix.studioApp.controller('PortalQuickLinksCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'QuickLinksService', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   QuickLinksService,   ContentTemplateService) {
		
		QuickLinksService.getQuickLinks( 
			function(quickLinks) {
				console.log(quickLinks);
				$scope.quickLinks = quickLinks;
			});
		
	}]);

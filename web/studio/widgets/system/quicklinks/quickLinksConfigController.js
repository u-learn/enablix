enablix.studioApp.controller('QuickLinksConfigController', 
			['$scope', '$state', '$stateParams', 'QuickLinksService', 'StateUpdateService', 'Notification',
	function( $scope,   $state,   $stateParams,   QuickLinksService,   StateUpdateService,   Notification) {
	
		$scope.quickLinksData = {"sections": []};
		
		QuickLinksService.getQuickLinks(function(quickLinks) {
			if (!isNullOrUndefined(quickLinks)) {
				$scope.quickLinksData = quickLinks;
			}
		});
		
		$scope.addNewQuickLinksSection = function() {
			$scope.quickLinksData.sections.unshift({
				sectionName: '',
				editing: true
			});
		};
		
		$scope.saveQuickLinkSection = function(quickLinkSection) {
			QuickLinksService.saveQuickLinkCategory(quickLinkSection, 
					function() {
						StateUpdateService.reload();
						Notification.primary("Saved successfully!");
						
					}, function() {
						Notification.error({message: "Error saving Quick Link section", delay: enablix.errorMsgShowTime});
					});
		};
		
	}
]);			

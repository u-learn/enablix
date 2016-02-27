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
		
		$scope.deleteQuickLink = function(quickLinkSec, indx) {

			if (quickLinkSec.links[indx] && quickLinkSec.links[indx].quickLinkIdentity) {
			
				QuickLinksService.deleteQuickLink(quickLinkSec.links[indx].quickLinkIdentity, 
					function(data) {
						quickLinkSec.links.splice(indx, 1);
					}, function(data) {
						Notification.error({message: "Error deleting Quick Link", delay: enablix.errorMsgShowTime});
					});
				
			}
		};
		
		$scope.cancelAddOrEdit = function(quickLinkSec) {
			quickLinkSec.editing = false;
			if (!quickLinkSec.sectionIdentity) {
				$scope.quickLinksData.sections.splice(0, 1);
			}
		};
		
		$scope.deleteQuickLinkSection = function(quickLinkSec) {
			if (quickLinkSec.sectionIdentity) {
				QuickLinksService.deleteQuickLinkSection(quickLinkSec.sectionIdentity, 
						function(data) {
							StateUpdateService.reload();
						}, function(data) {
							Notification.error({message: "Error deleting Quick Link Section", delay: enablix.errorMsgShowTime});
						});
			}
		};
		
	}
]);			

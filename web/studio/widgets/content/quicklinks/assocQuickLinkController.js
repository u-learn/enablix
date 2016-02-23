enablix.studioApp.controller('AssociateQuickLinkController', 
			['$scope', '$stateParams', 'QuickLinksService', 'StateUpdateService', 'StudioSetupService', 'Notification', '$modalInstance', 'quickLinkAssociation', 'contentInstanceIdentity',
	function( $scope,   $stateParams,   QuickLinksService,   StateUpdateService,   StudioSetupService,   Notification,   $modalInstance,   quickLinkAssociation,   contentInstanceIdentity) {
		
		$scope.quickLinkCategoryAssociations = quickLinkAssociation;
		
		$scope.close = function() {
			$modalInstance.close();
		}
		
		$scope.associateToCategory = function(assoc) {
			QuickLinksService.addQuickLink(assoc.category.identity, $stateParams.containerQId, contentInstanceIdentity,
				function(data) {
					Notification.primary("Added successfully!");
					assoc.associated = true;
					assoc.quickLinkIdentity = data.identity;
				}, 
				function(data) {
					Notification.error({message: "Error adding Quick Link", delay: enablix.errorMsgShowTime});
				});
		}
		
		$scope.dissociateFromCategory = function(assoc) {
			QuickLinksService.deleteQuickLink(assoc.quickLinkIdentity,
				function(data) {
					Notification.primary("Removed successfully!");
					assoc.associated = false;
					assoc.quickLinkIdentity = null;
				}, 
				function(data) {
					Notification.error({message: "Error removing Quick Link", delay: enablix.errorMsgShowTime});
				});
		}
		
	}
]);
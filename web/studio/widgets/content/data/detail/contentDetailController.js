enablix.studioApp.controller('ContentDetailCtrl', 
			['$scope', '$state', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification', 'AssocQuickLinkModalWindow', 'ManageRecoModalWindow',
	function( $scope,   $state,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification,   AssocQuickLinkModalWindow,   ManageRecoModalWindow) {
		
		var containerQId = $stateParams.containerQId;
		var elementIdentity = $stateParams.elementIdentity;
		
		$scope.$stateParams = $stateParams;
		$scope.$state = $state;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		var containerLabel = $scope.containerDef.label;
		
		if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
			
			$scope.containerDef = ContentTemplateService.getContainerDefinition(
					enablix.template, $scope.containerDef.linkContainerQId);
			
			if (isNullOrUndefined(containerLabel)) {
				containerLabel = $scope.containerDef.label;
			}
		}
		
		$scope.pageHeading = containerLabel + " Information";
		
		$scope.navToEdit = function() {
			$scope.goToDetailEdit(containerQId, elementIdentity);
		}
		
		$scope.navToContentList = function(parentIdentity) {
			$scope.goToContentList(containerQId, parentIdentity);
		};
		
		ContentDataService.getContentRecordData(enablix.templateId, containerQId, elementIdentity, 'STUDIO',
				function(data) {
					$scope.containerData = data;
				}, 
				function(data) {
					//alert('Error retrieving record data');
					Notification.error({message: "Error retrieving record data", delay: enablix.errorMsgShowTime});
				});
		
		
		
		$scope.deleteRecord = function() {
			
			ContentDataService.deleteContentData(containerQId, elementIdentity, 
				function(data) {
					Notification.primary("Deleted successfully!");
					var parentNode = $scope.getCurrentIndexNode ? $scope.getCurrentIndexNode().parentNode : null;
					$scope.postDataDelete(parentNode, elementIdentity);
				}, 
				function(data) {
					Notification.error({message: "Error deleting record", delay: enablix.errorMsgShowTime});
				});
			
		}
		
		$scope.manageQuickLinks = function() {
			AssocQuickLinkModalWindow.showAddQuickLinks(elementIdentity);
		}
		
		$scope.addToRecommendation = function() {
			ManageRecoModalWindow.showAddToRecoWindow(elementIdentity);
		}
		
	}
]);
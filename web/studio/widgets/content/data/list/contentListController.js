enablix.studioApp.controller('ContentListCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification', 'ContentUtil', '$modal',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification,   ContentUtil,   $modal) {
		
		var containerQId = $stateParams.containerQId;
		var parentIdentity = $stateParams.parentIdentity;
		
		$scope.containerDef = {};
		$scope.listHeaders = [];
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		var containerLabel = $scope.containerDef.label;
		
		if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
			
			$scope.containerDef = ContentTemplateService.getContainerDefinition(
					enablix.template, $scope.containerDef.linkContainerQId);
			
			if (isNullOrUndefined(containerLabel)) {
				containerLabel = $scope.containerDef.label;
			}
		}
		
		$scope.listHeaders = ContentUtil.getContentListHeaders($scope.containerDef);
		
		$scope.navToEdit = function(elementIdentity) {
			$scope.goToDetailEdit(containerQId, elementIdentity);
		}
		
		$scope.deleteRecord = function(elementIdentity) {
			
			ContentDataService.deleteContentData(containerQId, elementIdentity, 
				function(data) {
					Notification.primary("Deleted successfully!");
					var parentNode = $scope.getCurrentIndexNode ? $scope.getCurrentIndexNode().parentNode : null;
					if (parentNode && parentNode.children && parentNode.children.length > 0) {
						$scope.postDataDelete(parentNode, elementIdentity);
					}
					fetchData();
				}, 
				function(data) {
					Notification.error({message: "Error deleting record", delay: enablix.errorMsgShowTime});
				});
			
		}
		
		var fetchData = function() {
			ContentDataService.getContentData(enablix.templateId, containerQId, parentIdentity, 
				function(data) {
					$scope.listData = data;
					
					angular.forEach($scope.listData, function(item) {
						ContentUtil.decorateData($scope.containerDef, item);
					});
				}, 
				function(data) {
					//alert('Error retrieving list data');
					Notification.error({message: "Error retrieving list data", delay: enablix.errorMsgShowTime});
				});
		};
		
		fetchData();
		
		var showAddQuickLinks = function(contentIdentity) {
			var modalInstance = $modal.open({
			      templateUrl: 'views/content/quicklinks/contentQuickLinkAssociation.html',
			      size: '' // 'sm', 'lg'
			    });
		};
		
		$scope.tableRecordActions = 
			[{
				actionName: "Add to Quick Links",
				iconClass: "fa fa-link",
				tableCellClass: "edit",
				actionCallbackFn: showAddQuickLinks
			}];
		
		$scope.pageHeading = containerLabel;
		
		$scope.navToAddContent = function() {
			$scope.goToAddContent(containerQId, parentIdentity);
		};
		
		$scope.navToContentDetail = function(elementIdentity) {
			$scope.goToContentDetail(containerQId, elementIdentity);
		};
		
	}
]);
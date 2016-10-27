enablix.studioApp.controller('ContentSuggestDetailCtrl', 
			['$scope', '$state', '$stateParams', 'ContentApprovalService', 'ContentTemplateService', 'StateUpdateService', 'Notification', 'ActionNotesWindow', '$filter',
	function( $scope,   $state,   $stateParams,   ContentApprovalService,   ContentTemplateService,   StateUpdateService,   Notification,   ActionNotesWindow,   $filter) {
		
		var containerQId = $stateParams.containerQId;
		
		ContentApprovalService.getContent($stateParams.refObjectIdentity, function(record) {
			
			$scope.approvalRecord = record;
			$scope.containerData = record.objectRef.data;
			
			$scope.$stateParams = $stateParams;
			$scope.$state = $state;
			
			containerQId = record.objectRef.contentQId;
			
			$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
			var containerLabel = $scope.containerDef.label;
			
			if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
				$scope.containerDef = ContentTemplateService.getContainerDefinition(
						enablix.template, $scope.containerDef.linkContainerQId);
				
				if (isNullOrUndefined(containerLabel)) {
					containerLabel = $scope.containerDef.label;
				}
				
			}

			$scope.pageHeading = containerLabel + " - " + record.objectRef.contentTitle;
			
			$scope.isEditAllowed = ContentApprovalService.isActionAllowed('EDIT', $scope.approvalRecord);
			$scope.isApproveAllowed = ContentApprovalService.isActionAllowed('APPROVE', $scope.approvalRecord);
			$scope.isRejectAllowed = ContentApprovalService.isActionAllowed('REJECT', $scope.approvalRecord);
			
			$scope.actionHistoryTableData = record.actionHistory.actions;
			$scope.actionHistoryTableHeaders = 
				[{
					desc: "Action",
					valueFn: function(actRecord) { return $filter('translate')("content.approval.action." + actRecord.actionName); }
				},
				{
					desc: "User",
					valueKey: "actorName"
				},
				{
					desc: "Date",
					valueFn: function(actRecord) { return $filter('ebDate')(actRecord.actionDate); }
				},
				{
					desc: "Status",
					valueFn: function(actRecord) { return $filter('translate')("content.approval.status." + actRecord.toState); }
				},
				{
					desc: "Notes",
					valueFn: function(actRecord) { return actRecord.actionInput.notes; }
				}];
			
		});
		
		$scope.isEditAllowed = false;
		$scope.isApproveAllowed = false;
		$scope.isRejectAllowed = false;
		
		var isActionAllowed = function(actionName) {
			ContentApprovalService.isActionAllowed(actionName, $scope.approvalRecord);
		}
		
		$scope.approveContent = function() {
			var modalInstance = ActionNotesWindow.showWindow("Approve Content", "Approval notes");
			modalInstance.result.then(function(notes) {
				ContentApprovalService.approveContent($scope.approvalRecord.objectRef.identity, notes, function(data) {
					StateUpdateService.reload();
				});
			});
		}
		
		$scope.rejectContent = function() {
			var modalInstance = ActionNotesWindow.showWindow("Reject Content", "Rejection notes");
			modalInstance.result.then(function(notes) {
				ContentApprovalService.rejectContent($scope.approvalRecord.objectRef.identity, notes, function(data) {
					StateUpdateService.reload();
				});
			});
		}
		
		$scope.editContent = function() {
			StateUpdateService.goToContentRequestEdit($scope.approvalRecord.objectRef.identity);
		}
		
	}
]);
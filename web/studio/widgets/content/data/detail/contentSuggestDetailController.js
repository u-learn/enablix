enablix.studioApp.controller('ContentSuggestDetailCtrl', 
			['$scope', '$state', '$stateParams', 'ContentApprovalService', 'ContentTemplateService', 'StateUpdateService', 'Notification', 'ActionNotesWindow', 'ConfirmationModalWindow', '$filter',
	function( $scope,   $state,   $stateParams,   ContentApprovalService,   ContentTemplateService,   StateUpdateService,   Notification,   ActionNotesWindow,   ConfirmationModalWindow,   $filter) {
		
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
			
			$scope.isEditAllowed = ContentApprovalService.isActionAllowed(ContentApprovalService.actionEdit(), $scope.approvalRecord);
			$scope.isApproveAllowed = ContentApprovalService.isActionAllowed(ContentApprovalService.actionApprove(), $scope.approvalRecord);
			$scope.isRejectAllowed = ContentApprovalService.isActionAllowed(ContentApprovalService.actionReject(), $scope.approvalRecord);
			$scope.isWithdrawAllowed = ContentApprovalService.isActionAllowed(ContentApprovalService.actionWithdraw(), $scope.approvalRecord);
			
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
		$scope.isWithdrawAllowed = false;
		
		var isActionAllowed = function(actionName) {
			ContentApprovalService.isActionAllowed(actionName, $scope.approvalRecord);
		}
		
		$scope.approveContent = function() {
			ContentApprovalService.initApproveAction($scope.approvalRecord.objectRef.identity, function(data) {
				StateUpdateService.reload();
			});
		}
		
		$scope.rejectContent = function() {
			ContentApprovalService.initRejectAction($scope.approvalRecord.objectRef.identity, function(data) {
				StateUpdateService.reload();
			});
		}
		
		$scope.withdrawContent = function(record) {
			ContentApprovalService.initWithdrawAction(record, function(data) {
				StateUpdateService.reload();
			});
		}
		
		$scope.editContent = function() {
			StateUpdateService.goToContentRequestEdit($scope.approvalRecord.objectRef.identity);
		}
		
	}
]);
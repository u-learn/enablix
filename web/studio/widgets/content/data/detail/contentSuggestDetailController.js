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
			
			// check if the url has an action initiated
			if ($stateParams.action == 'approve') {
				
				if ($scope.isApproveAllowed) {
					$scope.approveContent();
				} else {
					Notification.error({message: "Approve action not allowed.", delay: enablix.errorMsgShowTime});
				}
				
			} else if ($stateParams.action == 'reject') {
				
				if ($scope.isRejectAllowed) {
					$scope.rejectContent();
				} else {
					Notification.error({message: "Reject action not allowed.", delay: enablix.errorMsgShowTime});
				}
			}
			
		});
		
		$scope.isEditAllowed = false;
		$scope.isApproveAllowed = false;
		$scope.isRejectAllowed = false;
		$scope.isWithdrawAllowed = false;
		
		var isActionAllowed = function(actionName) {
			ContentApprovalService.isActionAllowed(actionName, $scope.approvalRecord);
		};
		
		var reloadPage = function() {
			if ($stateParams.action == 'view') {
				StateUpdateService.reload();
			} else if ($state.includes("myaccount")) {
				StateUpdateService.goToMyContentRequestDetail($stateParams.refObjectIdentity);
			} else {
				StateUpdateService.goToContentRequestDetail($stateParams.refObjectIdentity);
			}
		};
		
		$scope.approveContent = function() {
			ContentApprovalService.initApproveAction($scope.approvalRecord, function(data) {
				reloadPage();
			});
		}
		
		$scope.rejectContent = function() {
			ContentApprovalService.initRejectAction($scope.approvalRecord, function(data) {
				reloadPage();
			});
		}
		
		$scope.withdrawContent = function(record) {
			ContentApprovalService.initWithdrawAction($scope.approvalRecord, function(data) {
				reloadPage();
			});
		}
		
		$scope.editContent = function() {
			if ($state.includes("myaccount")) {
				StateUpdateService.goToMyContentRequestEdit($stateParams.refObjectIdentity);
			} else {
				StateUpdateService.goToContentRequestEdit($stateParams.refObjectIdentity);
			}
		}
		
	}
]);
enablix.studioApp.controller('ContentRequestListCtrl', 
			['$scope', '$state', '$stateParams', 'ContentApprovalService', 'ActionNotesWindow', 'DataSearchService', 'ContentTemplateService', 'Notification', '$filter', 'StateUpdateService', 'AuthorizationService', 'isDraftPage',
	function( $scope,   $state,   $stateParams,   ContentApprovalService,   ActionNotesWindow,   DataSearchService,   ContentTemplateService,   Notification,   $filter,   StateUpdateService,   AuthorizationService,   isDraftPage) {
		
		$scope.tableHeading = isDraftPage ? "Content Drafts" : "Content Requests";
				
		$scope.pagination = {
			pageSize: enablix.defaultPageSize,
			pageNum: 0,
			sort: {
				field: "createdAt",
				direction: "DESC"
			}
		}
		
		$scope.tableHeaders =
			 [{
				 desc: "Content Type",
				 valueFn: function(record) {
					 var value = "";
					 var contentQId = record.objectRef.contentQId;
					 var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, contentQId);
					 if (!isNullOrUndefined(containerDef)) {
						 value = containerDef.label
					 }
					 return value;
				 }
			 },
		     {
				 desc: "Content Title",
				 valueFn: function(record) { return record.objectRef.contentTitle; },
				 sortProperty: "objectRef.contentTitle"
		     },
		     {
				 desc: "Status",
				 valueFn: function(record) { return $filter('translate')("content.approval.status." + record.currentState.stateName); },
		     	 sortProperty: "currentState.stateName"
		     },
		     {
		    	 desc: "Creation Date",
		    	 valueFn: function(record) { return $filter('ebDate')(record.createdAt); },
		    	 sortProperty: "createdAt"
		     },
		     {
		    	 desc: "Created By",
		    	 valueKey: "createdByName",
		    	 sortProperty: "createdByName"
		     }];
		
		$scope.contentRequestDetails = function(record) {
			
			if ($state.includes("myaccount")) {
				
				if (isDraftPage) {
					StateUpdateService.goToMyDraftContentDetail(record.objectRef.identity);
				} else {
					StateUpdateService.goToMyContentRequestDetail(record.objectRef.identity);
				}
				
			} else {
				StateUpdateService.goToContentRequestDetail(record.objectRef.identity)
			}
		}
		
		$scope.contentRequestEdit = function(record) {
			
			if ($state.includes("myaccount")) {
				
				if (isDraftPage) {
					StateUpdateService.goToMyDraftContentEdit(record.objectRef.identity);
				} else {
					StateUpdateService.goToMyContentRequestEdit(record.objectRef.identity);
				}
				
			} else {
				StateUpdateService.goToContentRequestEdit(record.objectRef.identity);
			}
		}
		
		$scope.contentRequestApprove = function(record) {
			ContentApprovalService.initApproveAction(record, function(data) {
				fetchSearchResult();
			});
		}

		$scope.contentRequestReject = function(record) {
			ContentApprovalService.initRejectAction(record, function(data) {
				fetchSearchResult();
			});
		}
		
		$scope.contentRequestDiscard = function(record) {
			ContentApprovalService.initDiscardAction(record.objectRef.identity, function(data) {
				fetchSearchResult();
			});
		} 
		
		$scope.contentRequestWithdraw = function(record) {
			ContentApprovalService.initWithdrawAction(record, function(data) {
				fetchSearchResult();
			});
		}
		
		var isActionAllowed = function(action, record) {
			return ContentApprovalService.isActionAllowed(action.actionName, record);
		};
		
		$scope.tableRecordActions = 
			[{
				actionName: ContentApprovalService.actionViewDetails(),
				tooltip: "Details",
				iconClass: "fa fa-eye",
				tableCellClass: "details",
				actionCallbackFn: $scope.contentRequestDetails,
				checkApplicable: function(record) { return true; }
			},
			{
				actionName: ContentApprovalService.actionEdit(),
				tooltip: "Edit",
				iconClass: "fa fa-pencil",
				tableCellClass: "edit",
				actionCallbackFn: $scope.contentRequestEdit,
				checkApplicable: isActionAllowed
			}];
		
		$scope.dataList = [];
		
		$scope.dataFilters = {};
		
		if ($state.includes("myaccount")) {
			$scope.dataFilters.createdBy = AuthorizationService.getCurrentUser().userId;
		}
		
		if (isDraftPage) {
			
			$scope.dataFilters.requestState = ContentApprovalService.stateDraft();
			
			$scope.tableRecordActions.push({
				actionName: ContentApprovalService.actionDiscard(),
				tooltip: "Discard",
				iconClass: "fa fa-times",
				tableCellClass: "remove",
				actionCallbackFn: $scope.contentRequestDiscard,
				checkApplicable: isActionAllowed
			});
			
		} else {
			$scope.dataFilters.requestStateNot = ContentApprovalService.stateDraft();
			$scope.tableRecordActions.push({
				actionName: ContentApprovalService.actionApprove(),
				tooltip: "Approve",
				iconClass: "fa fa-check",
				tableCellClass: "approve",
				actionCallbackFn: $scope.contentRequestApprove,
				checkApplicable: isActionAllowed
			});
			$scope.tableRecordActions.push({
				actionName: ContentApprovalService.actionReject(),
				tooltip: "Reject",
				iconClass: "fa fa-ban",
				tableCellClass: "remove",
				actionCallbackFn: $scope.contentRequestReject,
				checkApplicable: isActionAllowed
			});
			$scope.tableRecordActions.push({
				actionName: ContentApprovalService.actionWithdraw(),
				tooltip: "Withdraw",
				iconClass: "fa fa-undo",
				tableCellClass: "remove",
				actionCallbackFn: $scope.contentRequestWithdraw,
				checkApplicable: isActionAllowed
			});
		}
		
		var fetchSearchResult = function() {
			
			ContentApprovalService.getContentRequests($scope.dataFilters, $scope.pagination, function(dataPage) {
					
					$scope.dataList = dataPage.content;
					$scope.pageData = dataPage;
					
				}, function(errorData) {
					Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
				});
		}
		
		$scope.fetchResult = function(_pagination) {
			fetchSearchResult();
		}
		
		fetchSearchResult();
		
	}
]);
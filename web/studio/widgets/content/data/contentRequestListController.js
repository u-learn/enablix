enablix.studioApp.controller('ContentRequestListCtrl', 
			['$scope', '$state', '$stateParams', 'ContentApprovalService', 'ActionNotesWindow', 'DataSearchService', 'ContentTemplateService', 'Notification', '$filter', 'StateUpdateService',
	function( $scope,   $state,   $stateParams,   ContentApprovalService,   ActionNotesWindow,   DataSearchService,   ContentTemplateService,   Notification,   $filter,   StateUpdateService) {
		
		var DOMAIN_TYPE = "com.enablix.content.approval.model.ContentApproval";
				
		$scope.breadcrumbList = 
			[
		         { label: "Setup" },
		         { label: "Content Requests" }
			];
		
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
				 valueFn: function(record) { return record.objectRef.contentTitle; }
		     },
		     {
				 desc: "Status",
				 valueFn: function(record) { return $filter('translate')("content.approval.status." + record.currentState.stateName); }
		     },
		     {
		    	 desc: "Creation Date",
		    	 valueFn: function(record) { return $filter('ebDate')(record.createdAt); }
		     },
		     {
		    	 desc: "Created By",
		    	 valueKey: "createdByName"
		     }];
		
		$scope.contentRequestDetails = function(record) {
			StateUpdateService.goToContentRequestDetail(record.objectRef.identity)
		}
		
		$scope.contentRequestEdit = function(record) {
			StateUpdateService.goToContentRequestEdit(record.objectRef.identity);
		}
		
		$scope.contentRequestApprove = function(record) {
			var modalInstance = ActionNotesWindow.showWindow("Approve Content", "Approval notes");
			modalInstance.result.then(function(notes) {
				ContentApprovalService.approveContent(record.objectRef.identity, notes, function(data) {
					fetchSearchResult();
				});
			});
		}

		$scope.contentRequestReject = function(record) {
			var modalInstance = ActionNotesWindow.showWindow("Reject Content", "Rejection notes");
			modalInstance.result.then(function(notes) {
				ContentApprovalService.rejectContent(record.objectRef.identity, notes, function(data) {
					fetchSearchResult();
				});
			});
		}
		
		var isActionAllowed = function(action, record) {
			return ContentApprovalService.isActionAllowed(action.actionName, record);
		};
		
		$scope.tableRecordActions = 
			[{
				actionName: "VIEW_DETAILS",
				tooltip: "Details",
				iconClass: "fa fa-eye",
				tableCellClass: "details",
				actionCallbackFn: $scope.contentRequestDetails,
				checkApplicable: function(record) { return true; }
			},
			{
				actionName: "EDIT",
				tooltip: "Edit",
				iconClass: "fa fa-pencil",
				tableCellClass: "edit",
				actionCallbackFn: $scope.contentRequestEdit,
				checkApplicable: isActionAllowed
			},
			{
				actionName: "APPROVE",
				tooltip: "Approve",
				iconClass: "fa fa-check",
				tableCellClass: "approve",
				actionCallbackFn: $scope.contentRequestApprove,
				checkApplicable: isActionAllowed
			},
			{
				actionName: "REJECT",
				tooltip: "Reject",
				iconClass: "fa fa-ban",
				tableCellClass: "remove",
				actionCallbackFn: $scope.contentRequestReject,
				checkApplicable: isActionAllowed
			}];
		
		$scope.dataList = [];
		
		var fetchSearchResult = function() {
			
			DataSearchService.getSearchResult(DOMAIN_TYPE, {}, $scope.pagination, {}, function(dataPage) {
					
					$scope.dataList = dataPage.content;
					$scope.pageData = dataPage;
					
				}, function(errorData) {
					Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
				});
		}
		
		$scope.setPage = function(pageNum) {
			$scope.pagination.pageNum = pageNum;
			fetchSearchResult();
		}
		
		fetchSearchResult();
		
	}
]);
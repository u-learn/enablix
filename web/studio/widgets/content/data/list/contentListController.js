enablix.studioApp.controller('ContentListCtrl', 
			['$scope', '$state', '$stateParams', 'ContentDataService', 'ContentApprovalService', 'ContentIndexService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification', 'ContentUtil', 'QuickLinksService', 'AssocQuickLinkModalWindow', 'ManageRecoModalWindow', 'DataSearchService', 'AuthorizationService',
	function( $scope,   $state,   $stateParams,   ContentDataService,   ContentApprovalService,   ContentIndexService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification,   ContentUtil,   QuickLinksService,   AssocQuickLinkModalWindow,   ManageRecoModalWindow,   DataSearchService,   AuthorizationService) {
		
		var containerQId = $stateParams.containerQId;
		var parentIdentity = $stateParams.parentIdentity;
		var studioConfig = ContentTemplateService.getStudioConfig($stateParams.studioName);
		
		$scope.containerDef = {};
		$scope.listHeaders = [];
		
		$scope.pagination = {
				pageNum: 0,
				sort: {
					field: "createdAt",
					direction: "ASC"
				}
		};
		
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
					var parentNode = $scope.getCurrentIndexNode ? $scope.getCurrentIndexNode() : null;
					if (parentNode && parentNode.children && parentNode.children.length > 0) {
						$scope.postDataDelete(parentNode, elementIdentity);
					}
					$scope.fetchData();
				}, 
				function(data) {
					Notification.error({message: "Error deleting record", delay: enablix.errorMsgShowTime});
				});
			
		}
		
		$scope.fetchData = function() {
			
			ContentDataService.getContentData(enablix.templateId, containerQId, parentIdentity, 
				function(data) {
					$scope.listData = data.content;
					$scope.pageData = data;
					
					var currentIndexNode = $scope.$parent.getCurrentIndexNode ? $scope.$parent.getCurrentIndexNode() : null;
					if (!isNullOrUndefined(currentIndexNode) && studioConfig.navigableIndex) {
						ContentIndexService.refreshNodeChildren(currentIndexNode, $scope.listData, false);
					}
					
					angular.forEach($scope.listData, function(item) {
						ContentUtil.decorateData($scope.containerDef, item);
					});
				}, 
				function(data) {
					//alert('Error retrieving list data');
					Notification.error({message: "Error retrieving list data", delay: enablix.errorMsgShowTime});
				}, $scope.pagination);
		};
		
		$scope.fetchData();
		
		if ($state.includes('studio.list')) {
			$scope.tableRecordActions = 
				[{
					actionName: "Add to Quick Links",
					iconClass: "fa fa-link",
					tableCellClass: "edit",
					actionCallbackFn: AssocQuickLinkModalWindow.showAddQuickLinks
				},
				{
					actionName: "Add to Recommendations",
					iconClass: "fa fa-thumbs-up",
					tableCellClass: "details",
					actionCallbackFn: ManageRecoModalWindow.showAddToRecoWindow
				}];
		}
		
		$scope.pageHeading = containerLabel;
		
		$scope.navToAddContent = function() {
			$scope.goToAddContent(containerQId, parentIdentity);
		};
		
		$scope.navToContentDetail = function(elementIdentity) {
			$scope.goToContentDetail(containerQId, elementIdentity);
		};
		
		
		/*******************************************************
		 * Draft content list
		 *******************************************************/ 

		if ($state.includes('studio.list')) {
			
			$scope.draftPagination = {
					pageNum: 0,
					pageSize: 5,
					sort: {
						field: "createdAt",
						direction: "ASC"
					}
			};
			
			
			$scope.contentDraftDetails = function(recordObjectRefIdentity) {
				StateUpdateService.goToMyDraftContentDetail(recordObjectRefIdentity);
			}
			
			$scope.contentDraftEdit = function(recordObjectRefIdentity) {
				StateUpdateService.goToMyDraftContentEdit(recordObjectRefIdentity);
			}
					
			$scope.draftDataList = [];
			
			$scope.draftDataFilters = {
					"createdBy": AuthorizationService.getCurrentUser().userId,
					"requestState": ContentApprovalService.stateDraft(),
					"contentQId": containerQId
			};
			
			var fetchDraftContentList = function() {
				
				ContentApprovalService.getContentRequests($scope.draftDataFilters, $scope.draftPagination, function(dataPage) {
						
						var draftList = [];
						
						angular.forEach(dataPage.content, function(draftReq) {
							
							var dataItem = draftReq.objectRef.data;
							dataItem.identity = draftReq.objectRef.identity;
							
							draftList.push(dataItem);
						});
						
						$scope.draftDataList = draftList;
						$scope.draftPageData = dataPage;
						
					}, function(errorData) {
						Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
					});
			}
			
			$scope.fetchDraftResult = function(_pagination) {
				fetchDraftContentList();
			}
			
			fetchDraftContentList();
		}
	}
]);
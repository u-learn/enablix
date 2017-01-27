enablix.studioApp.controller('ContentConnListController', 
			['$scope', '$state', '$stateParams', '$filter', 'ContentConnectionService', 'StateUpdateService', 'Notification', 'ContentTemplateService',
	function( $scope,   $state,   $stateParams,   $filter,   ContentConnectionService,   StateUpdateService,   Notification,   ContentTemplateService) {
	
		$scope.pagination = {
				pageSize: enablix.defaultPageSize,
				pageNum: 0,
				sort: {
					field: "createdAt",
					direction: "DESC"
				}
			};
			
		$scope.tableHeaders =
			 [{
				 desc: "Mapping Name",
				 valueKey: "connectionName",
				 sortProperty: "connectionName"
			 },
		     {
				 desc: "Content Type",
				 valueFn: function(record) {
					 var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, record.contentQId);
					 return containerDef ? containerDef.label : ""; 
				}
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
		
		$scope.navToContentConnDetail = function(record) {
			StateUpdateService.goToContentConnDetail($stateParams.category, record.identity)
		}
		
		$scope.navToEditContentConn = function(record) {
			StateUpdateService.goToContentConnEdit($stateParams.category, record.identity);
		}
		
		$scope.navToAddContentConn = function() {
			StateUpdateService.goToContentConnAdd($stateParams.category);
		}
		
		$scope.deleteContentConn = function(record) {
			
			ContentConnectionService.deleteContentConnection(record.identity, function() {
				
				Notification.primary("Deleted successfully!");
				StateUpdateService.reload();
				
			}, function(errorData) {
				Notification.error({message: "Error deleting record. Please try later.", delay: enablix.errorMsgShowTime});
			});
		}
		
		$scope.tableRecordActions = 
			[{
				actionName: "Detail",
				tooltip: "Details",
				iconClass: "fa fa-eye",
				tableCellClass: "details",
				actionCallbackFn: $scope.navToContentConnDetail
			},
			{
				actionName: "Edit",
				tooltip: "Edit",
				iconClass: "fa fa-pencil",
				tableCellClass: "edit",
				actionCallbackFn: $scope.navToEditContentConn
			},
			{
				actionName: "Remove",
				tooltip: "Delete",
				iconClass: "fa fa-times",
				tableCellClass: "remove",
				actionCallbackFn: $scope.deleteContentConn
			}];
		
		$scope.categoryLabel = ContentConnectionService.getLabelForCategory($stateParams.category);
		$scope.dataList = [];
		
		$scope.dataFilters = {
			connCategory: ContentConnectionService.getCategoryTag($stateParams.category)
		};
		
		var fetchSearchResult = function() {
			
			ContentConnectionService.getContentConnectionList($scope.dataFilters, $scope.pagination, function(dataPage) {
					
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

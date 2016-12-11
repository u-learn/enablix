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
				 desc: "Content Mapping Name",
				 valueKey: "connectionName"
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
		    	 valueFn: function(record) { return $filter('ebDate')(record.createdAt); }
		     },
		     {
		    	 desc: "Created By",
		    	 valueKey: "createdByName"
		     }];
		
		$scope.navToContentConnDetail = function(record) {
			StateUpdateService.goToContentConnDetail(record.identity)
		}
		
		$scope.navToEditContentConn = function(record) {
			StateUpdateService.goToContentConnEdit(record.identity);
		}
		
		$scope.navToAddContentConn = function() {
			StateUpdateService.goToContentConnAdd();
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
			}];
		
		$scope.dataList = [];
		
		$scope.dataFilters = {};
		
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

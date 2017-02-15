enablix.studioApp.controller('ContentKitListCtrl', 
			['$scope', '$state', '$stateParams', '$filter', 'ContentKitService', 'StateUpdateService', 'Notification',
	function( $scope,   $state,   $stateParams,   $filter,   ContentKitService,   StateUpdateService,   Notification) {
	
		$scope.portalPage = $state.includes("portal");
		
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
				 desc: "Name",
				 valueKey: "name",
				 sortProperty: "name"
			 },
		     {
		    	 desc: "Created On",
		    	 valueFn: function(record) { return $filter('ebDate')(record.createdAt); },
		    	 sortProperty: "createdAt"
		     },
		     {
		    	 desc: "Created By",
		    	 valueKey: "createdByName",
		    	 sortProperty: "createdByName"
		     }];
		
		$scope.navToContentKitDetail = function(record) {
			StateUpdateService.goToContentKitDetail(record.identity)
		}
		
		$scope.navToEditContentKit = function(record) {
			StateUpdateService.goToContentKitEdit(record.identity);
		}
		
		$scope.navToAddContentKit = function() {
			StateUpdateService.goToAddContentConnKit();
		}
		
		$scope.navToPortalContentKitDetail = function(record) {
			StateUpdateService.goToPortalContentKitDetail(record.identity);
		}
		
		$scope.deleteContentKit = function(record) {
			
			ContentKitService.deleteContentKit(record.identity, function() {
				
				Notification.primary("Deleted successfully!");
				StateUpdateService.reload();
				
			}, function(errorData) {
				Notification.error({message: "Error deleting record. Please try later.", delay: enablix.errorMsgShowTime});
			});
		}
		
		if (!$scope.portalPage) { // do not show actions in portal page
			
			$scope.tableRecordActions = 
				[{
					actionName: "Detail",
					tooltip: "Details",
					iconClass: "fa fa-eye",
					tableCellClass: "details",
					actionCallbackFn: $scope.navToContentKitDetail
				},
				{
					actionName: "Edit",
					tooltip: "Edit",
					iconClass: "fa fa-pencil",
					tableCellClass: "edit",
					actionCallbackFn: $scope.navToEditContentKit
				},
				{
					actionName: "Remove",
					tooltip: "Delete",
					iconClass: "fa fa-times",
					tableCellClass: "remove",
					actionCallbackFn: $scope.deleteContentKit
				}];
			
		} else {
			$scope.tableRecordActions = 
				[{
					actionName: "Detail",
					tooltip: "Details",
					iconClass: "fa fa-eye",
					tableCellClass: "details",
					actionCallbackFn: $scope.navToPortalContentKitDetail
				}];
		}
		
		$scope.dataList = [];
		
		$scope.dataFilters = {};
		
		$scope.fetchSearchResult = function() {
			
			ContentKitService.getContentKitList($scope.dataFilters, $scope.pagination, function(dataPage) {
					
					$scope.dataList = dataPage.content;
					$scope.pageData = dataPage;
					
				}, function(errorData) {
					Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
				});
		}
		
		$scope.fetchSearchResult();
		
	}
]);			

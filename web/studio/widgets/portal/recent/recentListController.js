enablix.studioApp.controller('RecentListCtrl', 
		   ['$scope', '$stateParams', '$filter', 'DataSearchService', 'RecentDataService', 'Notification',
    function($scope,   $stateParams,   $filter,   DataSearchService,   RecentDataService,   Notification) {

		$scope.$stateParams = $stateParams;

		$scope.pagination = {
				pageSize: 20,
				pageNum: 0,
				sort: {
					field: "createdAt",
					direction: "DESC"
				}
			};
			
		$scope.tableHeaders =
			 [{
				 desc: "",
				 valueFn: function(record) { return record.recentData.updateType.toLowerCase(); },
			 	 tableCellClass: function(record) { 
			 		 return "recent-update-type " + "rct-" + record.recentData.updateType.toLowerCase(); 
			 	 }
			 },
		     {
				 desc: "Content Item",
				 valueFn: function(record) { return '<eb-nav-link hide-hierarchy="false" nav-link="tableRecord.recentNavContent"></eb-nav-link>'; },
				 dataType: 'angular-html'
		     },
		     {
		    	 desc: "Updated On",
		    	 valueFn: function(record) { return $filter('ebDate')(record.recentData.createdAt); },
		    	 sortProperty: "createdAt"
		     },
		     {
		    	 desc: "Updated By",
		    	 valueFn: function(record) { return record.recentData.createdByName; },
		    	 sortProperty: "createdByName"
		     }];
		
		$scope.dataFilters = DataSearchService.readUrlSearchFilters();
		
		$scope.filterMetadata = {
				lastXDays: {
					field: "createdAt",
					operator: "GTE",
					dataType: "DATE",
					dateFilter: {
						valueType: "LAST_X_DAYS"
					}
				}
		};
		
		$scope.dataList = [];
		
		$scope.fetchResult = function() {
			RecentDataService.getRecentDataList($scope.dataFilters, $scope.pagination, $scope.filterMetadata,
				function(data) {
					$scope.dataList = data.content;
					$scope.pageData = data;
				},
				function() {
					Notification.error({message: "Error fetching recent data list", delay: enablix.errorMsgShowTime});
				}
			);
		};
		
		$scope.fetchResult();
		
	}                                          
]);
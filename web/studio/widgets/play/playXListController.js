enablix.studioApp.controller('PlayXListCtrl', 
			['$scope', '$stateParams', 'PlayDefinitionService', 'Notification', 'QIdUtil', 'StateUpdateService', '$filter',
	function( $scope,   $stateParams,   PlayDefinitionService,   Notification,   QIdUtil,   StateUpdateService,   $filter) {
		
		$scope.prototypePlay = {};
		$scope.xPlayDefs = [];
		
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
				 desc: "Title",
				 valueFn: function(record) { return record.playTemplate.title; }
			 },
		     {
		    	 desc: "Creation Date",
		    	 valueFn: function(record) { return $filter('ebDate')(record.createdAt); }
		     },
		     {
		    	 desc: "Created By",
		    	 valueKey: "createdByName"
		     }];
		
		PlayDefinitionService.getPlayDefSummaryList({id: $stateParams.playDefId}, null, function(dataPage) {
			
			if (dataPage.content && dataPage.content.length == 1) {
				$scope.prototypePlay = dataPage.content[0];
			} else {
				Notification.error({message: "Play Definition not found", delay: enablix.errorMsgShowTime});
			}
			
			
		}, function(errorData) {
			Notification.error({message: "Error retrieving base play definition data", delay: enablix.errorMsgShowTime});
		});
		
		$scope.navToPlayDefDetail = function(record) {
			StateUpdateService.goToEditXPlay(record.id);
		}
		
		$scope.navToEditPlayDef = function(record) {
			StateUpdateService.goToEditXPlay(record.id);
		}
		
		$scope.tableRecordActions = 
			[{
				actionName: "Detail",
				tooltip: "Details",
				iconClass: "fa fa-eye",
				tableCellClass: "details",
				actionCallbackFn: $scope.navToPlayDefDetail
			},
			{
				actionName: "Edit",
				tooltip: "Edit",
				iconClass: "fa fa-pencil",
				tableCellClass: "edit",
				actionCallbackFn: $scope.navToEditPlayDef
			}];
		
		$scope.dataList = [];
		
		$scope.dataFilters = {
				prototypeId: $stateParams.playDefId
		};
		
		var fetchSearchResult = function() {
			
			PlayDefinitionService.getPlayDefSummaryList($scope.dataFilters, $scope.pagination, function(dataPage) {
					
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
		
		$scope.navToAddExecutablePlay = function() {
			StateUpdateService.goToAddXPlay($stateParams.playDefId);
		};
		
	}
]);
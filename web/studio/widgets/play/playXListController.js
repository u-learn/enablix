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
				 valueFn: function(record) { return record.playTemplate.title; },
				 sortProperty: "playTemplate.title"
			 },
			 {
				 desc: "Status",
				 valueFn: function(record) { return record.active ? "Active" : "In-active"; },
				 sortProperty: "active"
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
			StateUpdateService.goToEditXPlay(record.playTemplate.prototypeId, record.id);
		}
		
		$scope.navToEditPlayDef = function(record) {
			StateUpdateService.goToEditXPlay(record.playTemplate.prototypeId, record.id);
		}
		
		$scope.toggleActiveStatus = function(record) {
			
			PlayDefinitionService.updateActiveStatusOfPlayDef(record.id, !record.active, 
				function() {
					record.active = !record.active;
				}, function(errorData) {
					Notification.error({message: "Error updating status", delay: enablix.errorMsgShowTime});
				});
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
			},
			{
				actionName: "Activate",
				tooltip: function(record) {
					return record.active ? "De-activate" : "Activate";
				},
				iconClass: function(record) {
					return record.active ? "fa fa-toggle-on" : "fa fa-toggle-off";
				},
				tableCellClass: "toggle-switch",
				actionCallbackFn: $scope.toggleActiveStatus
			}];
		
		$scope.dataList = [];
		
		$scope.dataFilters = {
				prototypeId: $stateParams.playDefId
		};
		
		$scope.fetchSearchResult = function() {
			
			PlayDefinitionService.getPlayDefSummaryList($scope.dataFilters, $scope.pagination, function(dataPage) {
					
					$scope.dataList = dataPage.content;
					$scope.pageData = dataPage;
					
				}, function(errorData) {
					Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
				});
		}
		
		$scope.fetchSearchResult();
		
		$scope.navToAddExecutablePlay = function() {
			StateUpdateService.goToAddXPlay($stateParams.playDefId);
		};
		
	}
]);
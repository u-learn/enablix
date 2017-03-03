enablix.studioApp.controller('PlayExecutionDefCtrl', 
			['$scope', 'ConditionUtil', 'QIdUtil', 'PlayDefinitionService', '$modal',
    function ($scope,   ConditionUtil,   QIdUtil,   PlayDefinitionService,   $modal) {

		$scope.isCollapsed = false;
		
		$scope.tableHeaders =
			 [{
				 desc: "Action",
				 valueFn: function(record) { 
					 return PlayDefinitionService.getFirstActionName(record.actions); 
				 }
			 },
			 {
				 desc: "Type",
				 valueFn: function(record) {
					 return PlayDefinitionService.getActionType(record.actions);
				 }
			 },
			 {
				 desc: "Schedule",
				 valueFn: function(record) {
					 return "";
				 }
			 }];
		
		
		$scope.viewPlayActionDetail = function(record) {
			
			var modalInstance = $modal.open({
			      templateUrl: 'widgets/directive/play/execution/detail/playActionDetail.html',
			      size: 'lg', // 'sm', 'lg'
			      controller: 'PlayActionDetailCtrl',
			      backdrop: 'static',
			      resolve: {
			    	  checkpointDef: function() {
			    		  return record;
			    	  },
			    	  focusItems: function() {
			    		  return $scope.focusItems;
			    	  },
			    	  userGroupsDef: function() {
			    		  return $scope.userGroupsDef;
			    	  },
			    	  contentGroupsDef: function() {
			    		  return $scope.contentGroupsDef;
			    	  }
			      }
			});
			
			modalInstance.result.then(function(updtAction) {
				angular.copy(updtAction, record);
			});
		};
		
		$scope.editPlayActionDetail = function(record) {
			
		};
		
		$scope.tableRecordActions = 
			[{
				actionName: "View Details",
				tooltip: "Details",
				iconClass: "fa fa-eye",
				tableCellClass: "details",
				actionCallbackFn: $scope.viewPlayActionDetail
			},{
				actionName: "Edit",
				tooltip: "Edit",
				iconClass: "fa fa-pencil",
				tableCellClass: "edit",
				actionCallbackFn: $scope.editPlayActionDetail
			}];
		
		
		
}]);

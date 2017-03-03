enablix.studioApp.controller('PlayUserGroupsCtrl', 
			['$scope', '$modal',
    function ($scope,   $modal) {

		$scope.isCollapsed = false;
		
		$scope.viewUserGroupDetail = function(record) {
			
			var modalInstance = $modal.open({
			      templateUrl: 'widgets/directive/play/userGroup/detail/playUserGroupDetail.html',
			      size: 'lg', // 'sm', 'lg'
			      controller: 'PlayUserGroupDetailCtrl',
			      backdrop: 'static',
			      resolve: {
			    	  userGroup: function() {
			    		  return record;
			    	  },
			    	  focusItems: function() {
			    		  return $scope.focusItems;
			    	  }
			      }
			});
			
			modalInstance.result.then(function(updtUserGroup) {
				
				angular.copy(updtUserGroup, record);
				
				if (isNullOrUndefined(record.id)) {
					
					if (!$scope.userGroupsDef.userGroup) {
						$scope.userGroupsDef.userGroup = [];
					}
					
					record.id = generateUID();
					$scope.userGroupsDef.userGroup.push(record)
				}
			});
		};
		
		$scope.editUserGroupDetail = function(record) {
			
		};
		
		$scope.tableHeaders =
			 [{
				 desc: "Group",
				 valueKey: "name"
			 }];
		
		$scope.tableRecordActions = 
			[{
				actionName: "View Details",
				tooltip: "Details",
				iconClass: "fa fa-eye",
				tableCellClass: "details",
				actionCallbackFn: $scope.viewUserGroupDetail
			},{
				actionName: "Edit",
				tooltip: "Edit",
				iconClass: "fa fa-pencil",
				tableCellClass: "edit",
				actionCallbackFn: $scope.editUserGroupDetail
			}];
		
		
}]);

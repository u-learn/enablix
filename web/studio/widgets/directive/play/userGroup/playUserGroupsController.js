enablix.studioApp.controller('PlayUserGroupsCtrl', 
			['$scope', 
    function ($scope) {

		$scope.isCollapsed = false;
		
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
		
		$scope.viewUserGroupDetail = function(record) {
			
		};
		
		$scope.editUserGroupDetail = function(record) {
			
		};
		
}]);

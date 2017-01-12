enablix.studioApp.controller('PlayContentGroupsCtrl', 
			['$scope', 'ConditionUtil', 'QIdUtil', 'ContentTemplateService', '$modal',
    function ($scope,   ConditionUtil,   QIdUtil,   ContentTemplateService,   $modal) {

		$scope.isCollapsed = false;
		
		$scope.tableHeaders =
			 [{
				 desc: "Group",
				 valueKey: "name"
			 }];
		
		$scope.masterCorrelatedTypes = null;
		
		$scope.viewContentGroupDetail = function(record) {
			
			var modalInstance = $modal.open({
			      templateUrl: 'widgets/directive/play/contentGroup/detail/playContentGroupDetail.html',
			      size: 'lg', // 'sm', 'lg'
			      controller: 'PlayContentGroupDetailCtrl',
			      resolve: {
			    	  contentGroup: function() {
			    		  return record;
			    	  }
			      }
			});
			
		};
		
		$scope.editContentGroupDetail = function(record) {
			
		};
		
		$scope.tableRecordActions = 
			[{
				actionName: "View Details",
				tooltip: "Details",
				iconClass: "fa fa-eye",
				tableCellClass: "details",
				actionCallbackFn: $scope.viewContentGroupDetail
			},{
				actionName: "Edit",
				tooltip: "Edit",
				iconClass: "fa fa-pencil",
				tableCellClass: "edit",
				actionCallbackFn: $scope.editContentGroupDetail
			}];
		
}]);

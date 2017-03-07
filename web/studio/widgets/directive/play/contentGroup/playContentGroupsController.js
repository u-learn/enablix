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
			      backdrop: 'static',
			      resolve: {
			    	  contentGroup: function() {
			    		  return record;
			    	  },
			    	  focusItems: function() {
			    		  return $scope.focusItems;
			    	  }
			      }
			});
			
			modalInstance.result.then(function(updtContentGroup) {
				angular.copy(updtContentGroup, record);
				
				if (isNullOrUndefined(record.id)) {
					
					if (!$scope.contentGroupsDef.contentGroup) {
						$scope.contentGroupsDef.contentGroup = [];
					}
					
					record.id = generateUID();
					$scope.contentGroupsDef.contentGroup.push(record)
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

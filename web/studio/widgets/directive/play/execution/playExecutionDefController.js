enablix.studioApp.controller('PlayExecutionDefCtrl', 
			['$scope', 'ConditionUtil', 'QIdUtil', 'ContentTemplateService', 'InfoModalWindow',
    function ($scope,   ConditionUtil,   QIdUtil,   ContentTemplateService,   InfoModalWindow) {

		$scope.isCollapsed = false;
		
		var getFirstActionName = function(actions) {
			if (actions.email && actions.email.length > 0) {
				return actions.email[0].name;
			}
			return "";
		}
		
		$scope.tableHeaders =
			 [{
				 desc: "Action",
				 valueFn: function(record) { return getFirstActionName(record.actions); }
			 },{
				 desc: "Type",
				 valueFn: function(record) {
					 if (record.actions && record.actions.email) {
						 return "Email";
					 }
					 return "";
				 }
			 },{
				 desc: "Schedule",
				 valueFn: function(record) {
					 return "";
				 }
			 }];
		
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
		
		$scope.viewPlayActionDetail = function(record) {
			
		};
		
		$scope.editPlayActionDetail = function(record) {
			
		};
		
}]);

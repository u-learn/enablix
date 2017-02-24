enablix.studioApp.directive('ebContentStack', [
        'ContentTemplateService', 'Notification',
function(ContentTemplateService, Notification) {

	return {
		restrict: 'E',
		scope : {
			contentList: '=',
			contentDef: '=',
			readOnly: '@'
		},
		controller: "ContentStackController",
		templateUrl: "widgets/directive/contentstack/contentstack.html"
	};
}]);

enablix.studioApp.controller('ContentStackController', 
		['$scope', '$state', '$stateParams', '$filter', 'ContentTemplateService', 'StateUpdateService', 'Notification',
function( $scope,   $state,   $stateParams,   $filter,   ContentTemplateService,   StateUpdateService,   Notification) {

	$scope.readOnly = $scope.readOnly || false;
	
	$scope.contentTableHeaders =
		 [{
			 desc: "Type",
			 valueKey: "containerLabel",
			 sortProperty: "containerLabel"
		 },
	     {
	    	 desc: "Title",
	    	 valueKey: "label",
	    	 sortProperty: "label"
	     }];
	
	$scope.deleteContentRecord = function(_record, $event, _index) {
		if (_index >= 0) {
			$scope.contentList.splice(_index, 1);
		}
	}
	
	if (!$scope.readOnly) {
		
		$scope.contentTableRecordActions = 
			[{
				actionName: "Remove",
				tooltip: "Delete",
				iconClass: "fa fa-times",
				tableCellClass: "remove",
				actionCallbackFn: $scope.deleteContentRecord
			}];
		
	}
		
	$scope.contentSelected = function(selectedContentList) {
		$scope.contentList = selectedContentList;
	};
	
}
]);			

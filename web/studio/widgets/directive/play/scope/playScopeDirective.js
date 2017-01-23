enablix.studioApp.directive('ebxPlayScope', function() {

	return {
		restrict: 'E',
		scope : {
			scopeDef : "="
		},

		link: function(scope, element, attrs) {
			scope.isCollapsed = false;
		},

		controller: "PlayScopeCtrl",
		templateUrl: "widgets/directive/play/scope/playScope.html"
	}
});

// Controller
enablix.studioApp.controller('PlayScopeCtrl', 
		['$scope', 'ConditionUtil', 'QIdUtil', 'ContentTemplateService', 'InfoModalWindow', 'Notification',
function ($scope,   ConditionUtil,   QIdUtil,   ContentTemplateService,   InfoModalWindow,   Notification) {
	
	$scope.focusRecordList = [];
	$scope.focusItemRecord = null;
	
	var initFocusItemRecords = function() {
		
		if ($scope.scopeDef && $scope.scopeDef.focusItemRecord && $scope.focusItemRecord == null // null check to avoid multiple initializations 
				&& $scope.scopeDef.focusItemRecord.length > 0) {
		
			$scope.focusItemRecord = $scope.scopeDef.focusItemRecord[0]
		
			var focusLabelAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, $scope.focusItemRecord.itemQId);
			
			var focusRecordItemDef = {
				bounded : {
					refList : {
						datastore : {
							storeId: $scope.focusItemRecord.itemQId,
							dataId: "identity",
							dataLabel: focusLabelAttrId
						}
					}
				}
			};
			
			ContentTemplateService.getBoundedValueList(enablix.templateId, focusRecordItemDef, null, function(data) {
					$scope.focusRecordList = data;
				}, function(errorData) {
					Notification.error({message: "Error retrieving focus records", delay: enablix.errorMsgShowTime});
				});
		}
	}
	
	initFocusItemRecords();
	
	$scope.$watch('scopeDef', function(newValue, oldValue) {
		if (newValue && $scope.focusItemRecord == null) {
			initFocusItemRecords();
        }
    }, true);
			
}]);

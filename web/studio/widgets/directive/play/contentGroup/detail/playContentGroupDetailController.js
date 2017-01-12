enablix.studioApp.controller('PlayContentGroupDetailCtrl', 
			['$scope', 'ConditionUtil', 'QIdUtil', 'ContentTemplateService', '$modalInstance', 'contentGroup', 
    function ($scope,   ConditionUtil,   QIdUtil,   ContentTemplateService,   $modalInstance,   contentGroup) {

		$scope.contentGroup = contentGroup;
		
		$scope.cancelOperation = function() {
			$modalInstance.dismiss('cancel');
		}
		
}]);

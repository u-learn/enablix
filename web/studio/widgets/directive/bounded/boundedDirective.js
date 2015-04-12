enablix.studioApp.directive('ebBounded', [
        'ContentTemplateService',
function(ContentTemplateService) {

	return {
		restrict: 'E',
		scope : {
			selectValue: '=',
			contentDef: '='
		},
		link: function(scope, element, attrs) {
			
			var _dataDef = scope.contentDef;
			
			scope.name = _dataDef.id;
			scope.label = _dataDef.label;
			scope.id = _dataDef.qualifiedId;
			scope.options = [];
			
			var _uiDef = ContentTemplateService.getUIDefinition(enablix.template, _dataDef.qualifiedId);
			
			
			if (_dataDef.bounded.fixedList) {
				angular.forEach(_dataDef.bounded.fixedList.data, function(optItem) {
					scope.options.push({id: optItem.id, label: optItem.label});
				});
			}
			
		},
		templateUrl: "widgets/directive/bounded/bounded.html"
	};
}]);
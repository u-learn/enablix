enablix.studioApp.directive('ebText', [
        'ContentTemplateService',
function(ContentTemplateService) {

	return {
		restrict: 'E',
		scope : {
			textValue: '=',
			contentDef: '='
		},
		link: function(scope, element, attrs) {
			
			var _dataDef = scope.contentDef;
			
			scope.name = _dataDef.id;
			scope.label = _dataDef.label;
			scope.id = _dataDef.qualifiedId;
			scope.placeholder = "Enter " + _dataDef.label;
			scope.focus = attrs.focusvalue;
			
			var _uiDef = ContentTemplateService.getUIDefinition(enablix.template, _dataDef.qualifiedId);
			
			var textType = 'SHORT';
			if (!angular.isUndefined(_uiDef) && !angular.isUndefined(_uiDef.text) 
					&& !angular.isUndefined(_uiDef.text.type)) {
				textType = _uiDef.text.type;
			}
			
			scope.textType = textType;
		},
		templateUrl: "widgets/directive/text/textInput.html"
	};
}]);
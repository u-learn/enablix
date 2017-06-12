enablix.studioApp.directive('ebText', [
        'ContentTemplateService',
function(ContentTemplateService) {

	return {
		restrict: 'E',
		scope : {
			textValue: '=',
			contentDef: '=',
			contentRecord: '='
		},
		link: function(scope, element, attrs) {
			
			var _dataDef = scope.contentDef;
			
			var autoPopulate = false;
			var autoPopulateFlagSet = false;
			
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
				
				if (_uiDef.text.autoPopulate && _uiDef.text.autoPopulate.refContentItem
						&& _uiDef.text.autoPopulate.refContentItem.itemId) {
					
					var refContentItemId = _uiDef.text.autoPopulate.refContentItem.itemId;
					
					var canAutoPopulate = function() {
						
						if (!autoPopulateFlagSet) {
							autoPopulateFlagSet = true;
							autoPopulate = isNullOrUndefined(scope.textValue);
						}
						
						return autoPopulate;
					}
					
					scope.$watch(
						function() { return scope.contentRecord[refContentItemId]; }, 
						function(newValue, oldValue) {
							if (newValue !== oldValue && canAutoPopulate()) {
								scope.textValue = newValue;
							}
						});
					
					scope.onChange = function() {
						autoPopulate = false;
					}
				}
				
			}
			
			scope.textType = textType;
			
		},
		templateUrl: "widgets/directive/text/textInput.html"
	};
}]);
enablix.studioApp.directive('ebText', function() {

	var labelTemplate = '<label>{{label}}</label><br>';
	
	var shortTextTemplateHTML = labelTemplate + '<input id="{{id}}" name="{{name}}" '
			+ 'placeholder="{{placeholder}}" class="form-control" type="text" ng-model="textValue" size="80">';
	
	var longTextTemplateHTML = labelTemplate + '<input id="{{id}}" name="{{name}}" '
			+ 'placeholder="{{placeholder}}" class="form-control" type="text" ng-model="textValue" size="80">';
	
	return {
		restrict: 'E',
		scope : {
			textValue: '='
		},
		link: function(scope, element, attrs) {
			var _uiDef = eval("(" + scope.uiDef + ")"); 
			var _dataDef = eval("(" + attrs.contentDef + ")");
			scope.name = _dataDef.id;
			scope.label = _dataDef.label;
			scope.id = _dataDef.qualifiedId;
			scope.placeholder = "Enter " + _dataDef.label;
		},
		template: function(element, attrs) {
			
			var _uiDef = attrs.uiDef;
			
			if (angular.isUndefined(_uiDef) || angular.isUndefined(_uiDef.text) || 
					angular.isUndefined(_uiDef.text.type) || _uiDef.text.type == 'SHORT') {
				return shortTextTemplateHTML;
			}
			
			return longTextTemplateHTML;
		}
	}
});
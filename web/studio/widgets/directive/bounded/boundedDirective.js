enablix.studioApp.directive('ebBounded', [
        'ContentTemplateService', 'Notification',
function(ContentTemplateService, Notification) {

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
			
			ContentTemplateService.getBoundedValueList(enablix.templateId, _dataDef, null, 
					function(data) {
						scope.options = data;
					},
					function(data) {
						Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
						//alert("Error retrieving data.");
					});
			
		},
		templateUrl: "widgets/directive/bounded/bounded.html"
	};
}]);
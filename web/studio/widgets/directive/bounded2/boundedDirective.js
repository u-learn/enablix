enablix.studioApp.directive('ebBounded2', [
        'ContentTemplateService', 'Notification', '$filter',
function(ContentTemplateService,   Notification,   $filter) {

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
			scope.selectMode = _dataDef.bounded.multivalued ? "multiple" : "single";
			
			var selectedData = [];

			// have to watch for changes to get the data when values have been retrieved
			scope.$watch('selectValue', function(newValue, oldValue) {
				if (newValue) {
					selectedData = newValue;
				}
			});
			
			var _uiDef = ContentTemplateService.getUIDefinition(enablix.template, _dataDef.qualifiedId);
			
			ContentTemplateService.getBoundedValueList(enablix.templateId, _dataDef,
					function(data) {
						data = $filter('orderBy')(data, 'label', false);
						angular.forEach(data, function(opt) {
							for (var i = 0; i < selectedData.length; i++) {
								var selectVal = scope.selectValue[i];
								if (opt.id == selectVal.id) {
									opt.ticked = true;
									break;
								} 
							}
						});
						return data;
					},
					function(data) {
						scope.options = data;
					},
					function(data) {
						Notification.error({message: "Error retrieving data.", delay: enablix.errorMsgShowTime});
					});
			
		},
		templateUrl: "widgets/directive/bounded2/bounded.html"
	};
}]);
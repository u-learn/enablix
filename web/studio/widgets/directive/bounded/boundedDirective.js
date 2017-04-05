enablix.studioApp.directive('ebBounded', [
        'ContentTemplateService', 'Notification',
function(ContentTemplateService, Notification) {

	return {
		restrict: 'E',
		scope : {
			selectValue: '=',
			contentDef: '=',
			onSelectItem: '&',
			onRemoveItem: '&'
		},
		link: function(scope, element, attrs) {
			
			var _dataDef = scope.contentDef;
			
			scope.selectedVals = [];
			
			scope.name = _dataDef.id;
			scope.label = _dataDef.label;
			scope.id = _dataDef.qualifiedId;
			scope.options = [];
			scope.selectMultiple = _dataDef.bounded.multivalued;
			
			var _uiDef = ContentTemplateService.getUIDefinition(enablix.template, _dataDef.qualifiedId);
			
			ContentTemplateService.getBoundedValueList(enablix.templateId, _dataDef, null, 
					function(data) {
						scope.options = data;
						scope.options.sort(sortByLabelProp);
						
						angular.forEach(scope.selectValue, function(selVal) {
							if (isNullOrUndefined(selVal.label)) {
								for (var i = 0; i < scope.options.length; i++) {
									var opt = scope.options[i];
									if (opt.id === selVal.id) {
										selVal.label = opt.label;
										break;
									}
								}
							}
						});
					},
					function(data) {
						Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
						//alert("Error retrieving data.");
					});

			scope.onItemSelect = function($item, $model) {
				
				if (isNullOrUndefined(scope.selectValue)) {
					scope.selectValue = [];
				}
				
				var existIndex = null;
      		  	
				angular.forEach(scope.selectValue, function(itemValue, index) {
          		  	if (itemValue.id == $item.id) {
          		  		existIndex = index;
          		  		return false;
          		  	}
          		});
				
				if (isNullOrUndefined(existIndex)) {
					scope.selectValue.push($item);
					if (scope.onSelectItem) {
						scope.onSelectItem({'$item': $item});
					}
				}
				
			};
			
			scope.onItemRemove = function($item, $model) {
				
				var removeIndex = null;
      		  	
				angular.forEach(scope.selectValue, function(itemValue, index) {
          		  	if (itemValue.id == $item.id) {
          		  		removeIndex = index;
          		  		return false;
          		  	}
          		});
      		  	
          		if (!isNullOrUndefined(removeIndex)) {
          			scope.selectValue.splice(removeIndex, 1);
          			if (scope.onRemoveItem) {
						scope.onRemoveItem({'$item': $item});
					}
          		}
          		
			};
			
		},
		templateUrl: "widgets/directive/bounded/bounded.html"
	};
}]);
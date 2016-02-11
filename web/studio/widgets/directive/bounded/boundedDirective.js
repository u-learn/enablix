enablix.studioApp.directive('ebBounded', [
        'ContentTemplateService', 'Notification',
function(ContentTemplateService, Notification) {

/*	return {
        restrict: 'E',
        require: ['ngModel'],
        scope: true,
        templateUrl: "widgets/directive/bounded/bounded.html",
        compile: function (tElement, tAttrs) {
            
            return function link(scope, element, attrs, ctrls) {
            	scope.ngModel = ctrls[0];
            	scope.isMultiple = angular.isDefined(attrs.multiple) 
            	
            	scope.getValueMapper = function(itemObject){
            		return scope.valuePropGetter ? scope.valuePropGetter(itemObject) : itemObject;
            	}
            	
            	
            	scope.updateValueFromModel = function(modelValue){
            		if(scope.isMultiple){
            			var selectionArray = [];
            			angular.forEach(modelValue, function(modelItem, key){
            				var modelItemValue = scope.getValueMapper(modelItem);
            				selectionArray.push(modelItemValue);
            			});
            			scope.selectionModel = selectionArray;
            		}else{
            			var items = scope.itemsGetter(scope);
            			angular.forEach(items, function(item, key){
	            			var itemValue = scope.getValueMapper(item);
	            			if(itemValue == modelValue){
	            				scope.selectionModel = item;
	            				return false;
	            			}
	            		});
            		}
            	}
            	
            	if(scope.isMultiple){
            		scope.$watchCollection(attrs.ngModel, function(modelValue, oldValue) {
            			scope.updateValueFromModel(modelValue);
	                });	
            	}else{
	            	scope.$watch(attrs.ngModel, function(modelValue){
	            		scope.updateValueFromModel(modelValue);
	            	});
            	}
            	
            	//watch the items in case of async loading
            	//scope.$watch(attrs.items, function(){
            	//	scope.updateValueFromModel(scope.ngModel.$modelValue);
            	//});
            	
            	scope.onItemSelect = function(item, model){
            		var movelValue = scope.getValueMapper(item);
            		if(scope.isMultiple){
            			scope.ngModel.$viewValue.push(movelValue);
            		}else{
            			scope.ngModel.$setViewValue(movelValue);
            		}
            	}
            	
            	scope.onItemRemove = function(item, model){
            		var removedModelValue = scope.getValueMapper(item);
            		if(scope.isMultiple){
            		  var removeIndex = null;
            		  angular.forEach(scope.ngModel.$viewValue, function(itemValue, index){
	            		  if(itemValue == removedModelValue){
	            				removeIndex = index;
	            				return false;
	            			}
	            		});
	            		if(removeIndex){
	            		  scope.ngModel.$viewValue.splice(removeIndex, 1);
	            		}
            		}else{
            			scope.ngModel.$setViewValue(movelValue);
            		}
            	}
            	
            }
        }
	};*/
	return {
		restrict: 'E',
		scope : {
			selectValue: '=',
			contentDef: '='
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
          		}
          		
			};
			
		},
		templateUrl: "widgets/directive/bounded/bounded.html"
	};
}]);
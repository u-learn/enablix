enablix.studioApp.controller('ConditionAttrsCtrl', 
			['$scope', 'ConditionUtil', 'QIdUtil', 'ContentTemplateService', 'InfoModalWindow',
    function ($scope,   ConditionUtil,   QIdUtil,   ContentTemplateService,   InfoModalWindow) {

		$scope.condAttrs = [];
		$scope.initialized = false;
				
		$scope.$watch('conditionHolder', function(newValue, oldValue) {
			if (newValue) {
				initConditionAttrs();
            }
        }, true);
				
		var initConditionAttrs = function() {
			
			if ($scope.conditionHolder && !$scope.initialized) {
				
				$scope.condAttrs = [];
				$scope.condDefNodes = [];
				
				var globalParentQId = $scope.conditionOnContainer;
				var globalParentContainer = ContentTemplateService.getContainerDefinition(enablix.template, globalParentQId);
				
				ConditionUtil.walkCondition($scope.conditionHolder, function(_condNode, _nodeType) {
				
					if (_nodeType == ConditionUtil.basicType()) {
						
						var parentQId = globalParentQId;
						var parentContainer = globalParentContainer;
						
						if (!isNullOrUndefined(_condNode.onItem)) {
							var refItem = ContentTemplateService.getContentItem(parentContainer, _condNode.onItem);
							parentContainer = ContentTemplateService.getBoundedRefListContainer(refItem);
							parentQId = parentContainer.qualifiedId;
						}
						
						var attrId = _condNode.attribute.value;
						var contentItemDef = ContentTemplateService.getContentItem(parentContainer, attrId);
						
						var condAttr = {
								parentQId: parentQId,
								attributeId: attrId,
								label: contentItemDef.label,
								itemDef: contentItemDef,
								condDef: _condNode,
								values: []
							};
						
						angular.forEach(_condNode.value, function(condVal) {
							condAttr.values.push({id: condVal.value});
						});
						
						$scope.condAttrs.push(condAttr);
						
					}
					
				});
				
				$scope.initialized = true;
			}
		}
		
		$scope.condValueSelected = function($item, condAttr) {
			updateCondAttrValue(condAttr);
		}
		
		var updateCondAttrValue = function(condAttr) {
			condAttr.condDef.value = [];
			angular.forEach(condAttr.values, function(val) {
				condAttr.condDef.value.push({'value': val.id});
			});
		}
		
		$scope.condValueRemoved = function($item, condAttr) {
			updateCondAttrValue(condAttr, condNode);
		}
}]);

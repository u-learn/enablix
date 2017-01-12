enablix.studioApp.controller('ConditionAttrsCtrl', 
			['$scope', 'ConditionUtil', 'QIdUtil', 'ContentTemplateService', 'InfoModalWindow',
    function ($scope,   ConditionUtil,   QIdUtil,   ContentTemplateService,   InfoModalWindow) {

		$scope.condAttrs = [];
				
		$scope.$watch('conditionHolder', function(newValue, oldValue) {
			if (newValue) {
				initConditionAttrs();
            }
        }, true);
				
		var initConditionAttrs = function() {
			
			if ($scope.conditionHolder) {
				
				$scope.condAttrs = [];
				
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
								itemDef: contentItemDef
							};
						
						$scope.condAttrs.push(condAttr);
						
					}
					
				});
			}
		}
}]);

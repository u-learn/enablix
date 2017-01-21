enablix.studioApp.directive('ebxContentGroupCorrItems', [
            '$compile', 
	function($compile) {

		return {
			restrict: 'E',
			link: function(scope, element, attrs) {
				
				var typeHierarchy = attrs.typeHierarchy;
				
				var hierarchyLevel = attrs.hierarchyLevel || '0';
				hierarchyLevel = parseInt(hierarchyLevel, 10);
				
				var clazz = hierarchyLevel == 0 ? 'cg-corr-items' : 'cg-corr-items inner';
				
				var selectEnabled = attrs.selectedEnabled || true;
				scope.parentItem = scope.parentItem || {'_selected': true};
				
				//tree template
				var template =
						'<div class="' + clazz + '"><span ng-repeat="typeItem in ' + typeHierarchy + '" class="chckBox">' 
							+ '<input type="checkbox" ng-checked="(!typeItem.parent || typeItem.parent._selected) && typeItem._selected" '
								+ 'ng-click="toggleSelection(typeItem)" ng-disabled="(typeItem.parent && !typeItem.parent._selected)">'
							+ '<span class="chckBox-label">{{typeItem.label}}</span><br/>'
							+ '<ebx-content-group-corr-items type-hierarchy="typeItem.correlatedItem" '
								+ 'hierarchy-level="' + (hierarchyLevel + 1) + '"></ebx-content-group-corr-items>'
						'</span></div>';
				
				scope.toggleSelection = function(typeItem) {
					typeItem._selected = !typeItem._selected;
				}
				
				element.html('').append($compile(template)(scope));
			},
	
		}
    }
]);
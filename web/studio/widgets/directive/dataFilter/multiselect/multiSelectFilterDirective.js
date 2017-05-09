enablix.studioApp.directive('ebxMultiSelectFilter', [
        '$compile', 'Notification',
function($compile,   Notification) {

	return {
		restrict: 'E',
		scope : {
			filterDef: '=',
			selectedValues: '=',
			singleSelect: '='
		},
		link: function(scope, element, attrs) {
			
			
			scope.selectMultiple = !scope.singleSelect;
			
			scope.selectedValues = scope.selectedValues || [];
			
			scope.selected = {
					values: null
			};
			
			var checkAndUpdateModelValue = function() {
				if (!scope.singleSelect) {
					scope.selected.values = scope.selectedValues;
				} else if (scope.singleSelect && isArray(scope.selectedValues)) {
					scope.selected.values = scope.selectedValues.length > 0 ? scope.selectedValues[0] : null;
				}
			}
			
			checkAndUpdateModelValue();
			
			if (scope.filterDef.masterList) {
				
				scope.filterDef.masterList().then(function(data) {
					scope.options = data;
				}, function(error) {
					Notification.error({message: "Error retrieving data for " + scope.filterDef.name, delay: enablix.errorMsgShowTime});
				});
			}
			
			scope.onItemSelect = function() {
				
				if (scope.singleSelect) {
				
					scope.selectedValues = [];
					scope.selectedValues.push(scope.selected.values);
					
				} else {
					
					var newlyAdded = scope.selected.values[scope.selected.values.length - 1];
					if (scope.selectedValues!=null && scope.selectedValues != undefined 
							&& scope.selectedValues.indexOf(newlyAdded) === -1) {
						scope.selectedValues.push(newlyAdded);
					}
				}
			}
			
			scope.onItemRemove = function() {
				scope.selectedValues = scope.selected.values;
			}
			
			scope.$watch('selectedValues', function(newValue, oldValue) {
				if (newValue !== oldValue) {
					checkAndUpdateModelValue();
				}
			}, true);
			
		},
		templateUrl: "widgets/directive/dataFilter/multiselect/multiSelect.html"
	};
}]);
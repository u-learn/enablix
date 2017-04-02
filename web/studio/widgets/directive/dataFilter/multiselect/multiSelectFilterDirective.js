enablix.studioApp.directive('ebxMultiSelectFilter', [
        '$compile', 'Notification',
function($compile,   Notification) {

	return {
		restrict: 'E',
		scope : {
			filterDef: '=',
			selectedValues: '='
		},
		link: function(scope, element, attrs) {
			
			scope.selectedValues = scope.selectedValues || [];
			
			scope.selected = {
					values: scope.selectedValues
			};
			
			if (scope.filterDef.masterList) {
				
				scope.filterDef.masterList().then(function(data) {
					scope.options = data;
				}, function(error) {
					Notification.error({message: "Error retrieving data for " + scope.filterDef.name, delay: enablix.errorMsgShowTime});
				});
			}
			
			scope.onItemSelect = function() {
				var newlyAdded = scope.selected.values[scope.selected.values.length-1];
				if(scope.selectedValues.indexOf(newlyAdded) === -1){
					scope.selectedValues.push(newlyAdded);
				}
			}
			
			scope.onItemRemove = function() {
				scope.selectedValues = scope.selected.values;
			}
			
			scope.$watch('selectedValues', function(newValue, oldValue) {
				if (newValue !== oldValue) {
					scope.selected.values = newValue;
				}
			}, true);
			
		},
		templateUrl: "widgets/directive/dataFilter/multiselect/multiSelect.html"
	};
}]);
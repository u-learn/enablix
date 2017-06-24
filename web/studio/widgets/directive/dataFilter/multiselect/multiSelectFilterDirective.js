enablix.studioApp.directive('ebxMultiSelectFilter', [
        '$compile', 'Notification',
function($compile,   Notification) {

	return {
		restrict: 'E',
		scope : {
			filterDef: '=',
			selectedValues: '=',
			singleSelect: '=',
			ebLayout: "@",
			onSelectionChange: '=?'
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
					scope.$emit("msf:opt-init-complete", {filterDef: scope.filterDef, options: data});
				}, function(error) {
					Notification.error({message: "Error retrieving data for " + scope.filterDef.name, delay: enablix.errorMsgShowTime});
				});
			}
			
			function onSelectionChangeCallback() {
				if (scope.onSelectionChange) {
					scope.onSelectionChange();
				}
			}
			
			scope.onItemSelect = function() {
				
				if (scope.singleSelect) {
				
					scope.selectedValues = [];
					scope.selectedValues.push(scope.selected.values);
					onSelectionChangeCallback()
					
				} else {
					
					var newlyAdded = scope.selected.values[scope.selected.values.length - 1];
					if (scope.selectedValues!=null && scope.selectedValues != undefined 
							&& scope.selectedValues.indexOf(newlyAdded) === -1) {
						scope.selectedValues.push(newlyAdded);
						onSelectionChangeCallback();
					}
				}
			}
			
			scope.onItemRemove = function() {
				scope.selectedValues = scope.selected.values;
				onSelectionChangeCallback();
			}
			
			/* used for checkbox layout */
			scope.optionExists = function(_selOpt) {
				return scope.selectedValues.indexOf(_selOpt) > -1;
			}
			
			scope.toggleSelection = function(_selOpt) {
				
				var idx = scope.selectedValues.indexOf(_selOpt);
		        
				if (idx > -1) {
		        	scope.selectedValues.splice(idx, 1);
		        } else {
		        	scope.selectedValues.push(_selOpt);
		        }
		        
		        onSelectionChangeCallback();
			}
			
			scope.defaultOptLimit = 3;
			scope.currentOptLimit = scope.defaultOptLimit;
			scope.setOptLimit = function(_limit) {
				scope.currentOptLimit = _limit;
			}
			
			scope.$watch('selectedValues', function(newValue, oldValue) {
				if (newValue !== oldValue) {
					checkAndUpdateModelValue();
				}
			}, true);
			
			
			
		},
		templateUrl: function(elem, attr) {
			var layout = attr.ebLayout || "dropdown";
			return "widgets/directive/dataFilter/multiselect/multiSelect-" + layout + ".html"
		}
	};
}]);
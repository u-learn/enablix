enablix.studioApp.directive('ebxContainerFilters', [
        'ContentTemplateService', '$q',
function(ContentTemplateService,   $q) {

	return {
		restrict: 'E',
		scope : {
		    containerQId: '=',
			onSearch: '=?'
		},
		link: function(scope, element, attrs) {
			
			var filterMetadata = {};
			
			scope.filterDefs = [];
			
			var containerDef = ContentTemplateService.getConcreteContainerDefinition(
					enablix.template, scope.containerQId);
			
			var setContainerFilters = function(_containerDef) {
				
				var filterDefs = [];
				
				angular.forEach(_containerDef.contentItem, function(contentItemDef) {
					
					if (ContentTemplateService.isBoundedListItem(contentItemDef)) {
						
						var filterId = "df_" + contentItemDef.id;
						
						var filterDef = {
							id: filterId,
							type: "multi-select",
							name: contentItemDef.label,
							masterList: function() { // This must return a promise
								
								var deferred = $q.defer();
								
								ContentTemplateService.getBoundedValueList(enablix.templateId, contentItemDef, null, 
										function(data) {
											data.sort(sortByLabelProp);
											deferred.resolve(data);
										},
										function(data) {
											Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
										});
								
								return deferred.promise;
							},
							validateBeforeSubmit: function(_selectedValues) { return true; },
							filterValueTransformer: function(_selectedValues) {
								
								var returnVal = [];
								
								angular.forEach(_selectedValues, function(val) {
									if (!isNullOrUndefined(val)) {
										returnVal.push(val.id);
									}
								});
								
								return returnVal;
							},
							defaultValue: function() { return []; }
						}
						
						filterDefs.push(filterDef);
						
						filterMetadata[filterId] = {
							"field" : contentItemDef.id + ".id",
		 					"operator" : "IN",
		 					"dataType" : "STRING"
						};
						
					}
					
				});
				
				scope.filterDefs = filterDefs;
				
				scope.$emit("cont-filter:filter-init-complete", filterDefs);
				
			}
			
			scope.onContainerSearch = function(_filterValues) {
				if (scope.onSearch) {
					scope.onSearch(_filterValues, filterMetadata);
				}
			}

			if (containerDef) {
				setContainerFilters(containerDef);
				if (scope.filterDefs.length == 0) {
					scope.onContainerSearch({});
				}
			}
			
			scope.toggleSidebar = function($event) {
				var elem = $event.currentTarget;
				$(elem).parent().parent().parent().toggleClass('closed');
			}
			
		},
		templateUrl: "widgets/directive/dataFilter/containerFilters.html",
	};
}]);
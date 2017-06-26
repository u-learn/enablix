enablix.studioApp.directive('ebxContainerFilters', [
        'ContentTemplateService', '$q', 'DataSearchService',
function(ContentTemplateService,   $q,   DataSearchService) {

	return {
		restrict: 'E',
		scope : {
		    containerQId: '=',
			onSearch: '=?'
		},
		link: function(scope, element, attrs) {
			
			var filterMetadata = {};
			
			scope.filterDefs = [];
			
			var filterOpts = {};
			var filterOptItemCounts = {};
			
			var containerDef = ContentTemplateService.getConcreteContainerDefinition(
					enablix.template, scope.containerQId);
			
			var setContainerFilters = function(_containerDef) {
				
				var filterDefs = [];
				
				angular.forEach(_containerDef.contentItem, function(contentItemDef) {
					
					if (ContentTemplateService.isBoundedListItem(contentItemDef)) {
						
						var filterId = contentItemDef.id;
						
						var filterDef = {
							id: filterId,
							type: "multi-select",
							name: contentItemDef.label,
							masterList: function() { // This must return a promise
								
								var deferred = $q.defer();
								
								ContentTemplateService.getBoundedValueList(enablix.templateId, contentItemDef, null, 
										function(data) {
											
											data.sort(sortByLabelProp);
											
											filterOpts[filterId] = data;
											updateFilterOptItemCnt(filterId, data);
											
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
				
				scope.$watchCollection('filterDefs', function(newValue, oldValue) {
					if (newValue != oldValue) {
						scope.$emit("cont-filter:filter-init-complete", filterDefs);
					}
				})
				
			}
			
			var updateFilterOptsItemCount = function() {

				if (!angular.equals(filterOpts, {}) && !angular.equals(filterOptItemCounts, {})) {
				
					angular.forEach(filterOpts, function(optList, filterId) {
						updateFilterOptItemCnt(filterId, optList);
					});
				}
			}
			
			var updateFilterOptItemCnt = function(_filterId, _opts) {
				
				var filterItemCnts = filterOptItemCounts[_filterId];
				
				if (filterItemCnts) {
				
					angular.forEach(_opts, function(opt) {
					
						opt.count = 0;
						var itemCnt = filterItemCnts.length;
						
						for (var i = 0; i < itemCnt; i++) {
							var filterItemCnt = filterItemCnts[i];
							if (filterItemCnt.id === opt.id) {
								opt.count = filterItemCnt.count;
								break;
							}
						}
					});
				}
			} 
			
			var findAndUpdateFilterOptItemCount = function(_filterValues) {
				
				removeNullOrEmptyProperties(_filterValues);
				
				DataSearchService.getContainerCountByRefListItems(scope.containerQId, _filterValues, filterMetadata,
					function(data) {
						filterOptItemCounts = data;
						updateFilterOptsItemCount();
					});
			}
			
			scope.onContainerSearch = function(_filterValues) {
				if (scope.onSearch) {
					scope.onSearch(_filterValues, filterMetadata);
					findAndUpdateFilterOptItemCount(_filterValues);
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
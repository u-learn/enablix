enablix.studioApp.directive('ebxRecordCardFilters', [
        'ContentTemplateService', '$q', 'DataSearchService',
function(ContentTemplateService,   $q,   DataSearchService) {

	return {
		restrict: 'E',
		scope : {
		    containerQId: '=?',
		    enclosureId: '=?',
		    enclosureChildQId: '=?',
			subContainerList: '=',
			onSelectionChange: '='
		},
		link: function(scope, element, attrs) {
			
			scope.filterDefs = [];
			var subContOptList = [];
			
			var fetchSubContList = function() {
				
				if (isNullOrUndefined(scope.enclosureId)) {
					
					var containerDef = ContentTemplateService.getConcreteContainerDefinition(
							enablix.template, scope.containerQId);
					
					angular.forEach(containerDef.container, function(subContDef) {
						subContOptList.push({id: subContDef.qualifiedId, label: subContDef.label, count: 0});
					});
					

					
					if (ContentTemplateService.hasContentStackConfigItem(containerDef)) {
						angular.forEach(scope.subContainerList, function(subCntnr) {
							if (subCntnr.category === 'content-stack') {
								subContOptList.push({id: subCntnr.qualifiedId, label: subCntnr.label, count: 0});
							}
						})
					}

					subContOptList.sort(sortByLabelProp);
					
				} else {

					if (isNullOrUndefined(scope.enclosureChildQId) || scope.enclosureChildQId === "") {
					
						var enclDef = ContentTemplateService.getPortalEnclosureDefinition(scope.enclosureId);

						angular.forEach(enclDef.childContainer, function(childRef) {
							
							var childContDef = ContentTemplateService.getContainerDefinition(
									enablix.template, childRef.id);
			
							if (!isNullOrUndefined(childContDef)) {
								subContOptList.push({id: childContDef.qualifiedId, label: childContDef.label, count: 0});
							}
							
						});
						
					}
					
				}
				
				subContOptList.sort(sortByLabelProp);
			}
			
			var setContainerFilters = function(_containerDef) {
				
				var filterDefs = [];
				
				var subContFilterDef = {
					id: "subContainerFilter",
					type: "multi-select",
					name: "Content Types",
					masterList: function() { // This must return a promise
						
						var deferred = $q.defer();
						deferred.resolve(subContOptList);

						return deferred.promise;
					},
					defaultValue: function() { return []; }
				}
				
				filterDefs.push(subContFilterDef);
				
				scope.filterDefs = filterDefs;
				
				updateSubContItemCount();

				if (scope.subContainerList && scope.subContainerList.length > 0 && !scope.subContainerList[0].recordCount) {
					scope.$watch('subContainerList', function(newValue, oldValue) {
						updateSubContItemCount();
					}, true);
				}

			}
			
			var updateSubContItemCount = function() {
				
				var filterItemCnts = scope.subContainerList;
				
				var j = subContOptList.length;
				
				while (j--) {
					
					var opt = subContOptList[j];
					opt.count = 0;
					
					if (filterItemCnts) {
						
						var itemCnt = filterItemCnts.length;
						
						for (var i = 0; i < itemCnt; i++) {
							var filterItemCnt = filterItemCnts[i];
							if (filterItemCnt.qualifiedId === opt.id) {
								opt.count = filterItemCnt.recordCount;
								break;
							}
						}
						
						if (opt.count === 0) {
							subContOptList.splice(j, 1);
						}
					}
				}
				
			}
			
			scope.onRecordFilterSelection = function(_filterValues) {
				if (scope.onSelectionChange) {
					scope.onSelectionChange(_filterValues.subContainerFilter ? _filterValues.subContainerFilter : []);
				}
			}

			fetchSubContList();
			
			if (subContOptList.length !== 0) {
				setContainerFilters();
			}
			
			scope.toggleSidebar = function($event) {
				var elem = $event.currentTarget;
				$(elem).parent().parent().parent().toggleClass('closed');
			}
			
		},
		templateUrl: "widgets/directive/dataFilter/recordCardFilters.html",
	};
}]);
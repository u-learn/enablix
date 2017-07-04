enablix.studioApp.directive('ebxSearchBox', [
				 'StateUpdateService', '$q', 'DataSearchService', 'ContentTemplateService', 'ActivityAuditService', 'ContentUtil',
		function (StateUpdateService,   $q,   DataSearchService,   ContentTemplateService,   ActivityAuditService,   ContentUtil) {
			return {
				restrict : 'E',
				scope : {
				},
				link : function(scope, element, attrs) {
					
					var suggestionOn = true;
					
					if (!suggestionOn) {
						
						element.bind("keydown keypress", function (event) {
							
							var keyCode = event.which || event.keyCode;
							
							// If enter key is pressed
				            if (keyCode === 13) {
				                scope.$apply(function() {
				                    scope.search();
				                });
				                event.preventDefault();
				            }
						});
						
						scope.search = function() {
							if (!isNullOrUndefined(scope.searchText) && scope.searchText.length > 0) {
								StateUpdateService.goToPortalSearch(scope.searchText);
							}
						}
					}
					
					if (suggestionOn) {
						
						var itemContainerList = ContentTemplateService.getContainersByBusinessCategory("BUSINESS_DIMENSION");
						
						var asyncData = { template : enablix.pTemplate };
						
						itemContainerList.forEach(function(containerQId){
							
							var qualifiedId = containerQId.qualifiedId;
							
							var value = DataSearchService.promiseContainerDataSearchResult(qualifiedId).then(
											function(container){
												return _.get(container,"data.content");
											});
							
							asyncData[qualifiedId] = value;
						});
						
						var mountPoint = "#enablix-search";
						
						function onTrueEnter(payload) {
							var node = payload.node;
							var searchText = payload.query;
						
							if (node && node.type) {
								
								var uri = node.asUri();
								
								var suggestedSearch = {
										searchTerm: searchText,
										typedText: payload.typedText,
										suggestion: uri,
										suggestionType: node.type
								};
								
								StateUpdateService.setStateChangeActivityContext({
									atChannel: "WEB",
									atContext: "SEARCH",
									atActivityOrigin: "TYPEAHEAD",
									atContextTerm: searchText
								});
								
								if (node.type === "BizDimensionNode") {
									
									StateUpdateService.goToPortalContainerList(uri.containerQId, null, {}, searchText);
									
								} else if (node.type === 'BizContentNode') {
									
									StateUpdateService.goToPortalContainerList(uri.containerQId, "tile", {}, searchText);
									
								} else if (node.type === "BizDimEntSubNode") {
							
									ContentUtil.navToTileBasedSubContainerList(uri.containerQId, 
															uri.subContainerQId, uri.parentIdentity, null, searchText);
									
								} else if (node.type === "BizDimensionEntity") {
									
									StateUpdateService.goToPortalContainerDetail(uri.containerQId, uri.identity);
								}
								
								ActivityAuditService.auditSearchSuggestion(suggestedSearch);	
									
							} else if (!isNullOrUndefined(searchText) && searchText.length > 0) {
								StateUpdateService.goToPortalSearch(searchText);
							} 		
							
						}
						
						enablix.searchComponent = new enablix.SearchComponent({ mountPoint : mountPoint, asyncData : asyncData });
						enablix.searchComponent.onTrueEnter = onTrueEnter;
					}
					
				},
				templateUrl : "widgets/directive/search/searchBox.html"
			};
		} ]);